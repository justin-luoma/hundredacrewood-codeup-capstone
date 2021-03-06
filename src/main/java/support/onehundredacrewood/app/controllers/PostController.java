package support.onehundredacrewood.app.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import support.onehundredacrewood.app.dao.models.Comment;
import support.onehundredacrewood.app.dao.models.Post;
import support.onehundredacrewood.app.dao.models.Topic;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.CommentRepo;
import support.onehundredacrewood.app.dao.repositories.PostRepo;
import support.onehundredacrewood.app.dao.repositories.TopicRepo;
import support.onehundredacrewood.app.dao.repositories.UserRepo;
import support.onehundredacrewood.app.services.NotificationSenderService;
import support.onehundredacrewood.app.services.ProfanityFilter;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class PostController {
    private final PostRepo postRepo;
    private final TopicRepo topicRepo;
    private final UserRepo userRepo;
    private final CommentRepo commentRepo;
    private final NotificationSenderService notificationSvc;

    public PostController(PostRepo postRepo, TopicRepo topicRepo,
                          UserRepo userRepo, CommentRepo commentRepo,
                          NotificationSenderService notificationSvc) {
        this.postRepo = postRepo;
        this.topicRepo = topicRepo;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
        this.notificationSvc = notificationSvc;
    }

    @GetMapping("/posts")
    public String showPosts(Model vModel) {
        List<Post> posts = postRepo.findAll();
        posts.sort(Comparator.comparing(Post::getCreated).reversed());
        vModel.addAttribute("posts", posts);
        return "post/index";
    }

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isFollowing = false;
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated()) {
            User principal = ( User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userRepo.findById(principal.getId()).get();
            isFollowing = Post.isFollowing(user.getFollowedPosts(), id);
        }
        Post post = postRepo.findById(id);
        if (post == null) {
            return "redirect:/";
        }


        model.addAttribute("post", post);
        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("comment", new Comment());
        return "post/one";
    }

    @GetMapping("/posts/create")
    public String createPostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("selectTopic", topicRepo.findAll());
        return "post/create";
    }

    @PostMapping("/posts/create")
    public String createPost(@ModelAttribute Post post, @RequestParam(name="selectTopic") Long[] topics) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUser(user);
        post.setCreated(LocalDateTime.now());
        post.setReported(false);
        post.setLocked(false);
        List<Topic> newTopics = new ArrayList<>();
        for (Long topic: topics) {
            newTopics.add(topicRepo.findById(topic).get());
        }
        post.setTopics(newTopics);
        post.setBody(ProfanityFilter.getCensoredText(post.getBody()));
        post.setTitle(ProfanityFilter.getCensoredText(post.getTitle()));
        Post newPost = postRepo.save(post);
        return "redirect:/posts/"+ newPost.getId();
    }

    @GetMapping("/posts/{postId}/update")
    public String editPost(@PathVariable long postId, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepo.findById(postId);
        if (user.getId() != post.getUser().getId()) {
            return "redirect:/";
        }
        model.addAttribute("post", post);
        model.addAttribute("allTopics", topicRepo.findAll());
        return "post/edit";
    }

    @PostMapping("/posts/{id}/update")
    public String updatePost(@PathVariable long id, @ModelAttribute Post post) {
        Post postUpdate = postRepo.findById(id);
        postUpdate.setTitle(ProfanityFilter.getCensoredText(post.getTitle()));
        postUpdate.setBody(ProfanityFilter.getCensoredText(post.getBody()));
        postUpdate.setTopics(post.getTopics());
        postRepo.save(postUpdate);

        notificationSvc.ProcessPostAction(postUpdate, false, null);
        return "redirect:/posts/"+ post.getId();
    }

    @GetMapping("/posts/myposts")
    public String showUserPosts( Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("posts", postRepo.findAllByUserOrderByCreatedDesc(user));
        return "post/index";
    }

    @PostMapping("/posts/follow")
    public String followPosts(@RequestParam(name = "id") long id, @RequestHeader(value = "referer") final String referer) {
        User principal = ( User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepo.findById(id);
        User user = userRepo.findById(principal.getId()).get();


        user.followPost(post);
        postRepo.save(post);

        return "redirect:" + referer;
    }

    @PostMapping("/posts/comment")
    public String comment(@ModelAttribute Comment comment,
                          @RequestParam(name = "postId") long postId,
                          @RequestParam(name = "userId") long userId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getId() != userId) {
            return "redirect:/";
        }
        User user = userRepo.findById(userId).get();
        Post post = postRepo.findById(postId);
        if (post.isLocked())
            return "redirect:/posts/" + postId;
        comment.setCreated(LocalDateTime.now());
        comment.setPost(post);
        comment.setReported(false);
        comment.setUser(user);
        comment.setBody(ProfanityFilter.getCensoredText(comment.getBody()));
        commentRepo.saveAndFlush(comment);
        notificationSvc.ProcessPostAction(post, true, userId);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/report")
    public String report(@RequestParam(name = "id") long postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated()) {
            Post post = postRepo.findById(postId);
            post.setReported(true);
            postRepo.save(post);

            return "redirect:/posts/" + postId;
        }

        return "redirect:/";
    }

    @PostMapping("/posts/clear")
    public String clearReport(@RequestParam(name = "id") long postId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principal.isAdmin()) {
            return "redirect:/";
        }

        Post post = postRepo.findById(postId);
        post.setReported(false);
        postRepo.save(post);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/lock-toggle")
    public String lockPost(@RequestParam(name = "id") long postId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principal.isAdmin()) {
            return "redirect:/";
        }

        Post post = postRepo.findById(postId);
        post.setLocked(!post.isLocked());
        postRepo.save(post);
        return "redirect:/posts/" + postId;
    }


    @PostMapping("/posts/delete")
    public String deletePost(@RequestParam(name = "id") long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepo.findById(id);
        if (!principal.isAdmin() && principal.getId() != post.getUser().getId()) {
            System.out.println(principal.getId());
            System.out.println(post.getUser().getId());
            return "redirect:/";
        }

        postRepo.deleteById(id);
        return "redirect:/posts";
    }

    @PostMapping("/posts/comment/report")
    public String reportComment(@RequestParam(name = "id") long commentId, @RequestParam(name = "postId") long postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated()) {
            commentRepo.findById(commentId).ifPresent(c -> {
                c.setReported(true);
                commentRepo.saveAndFlush(c);
            });
            return "redirect:/posts/" + postId;
        }

        return "redirect:/";
    }

    @PostMapping("/posts/comment/clear")
    public String clearReportedComment(@RequestParam(name = "id") long commentId, @RequestParam(name = "postId") long postId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principal.isAdmin()) {
            return "redirect:/";
        }

        commentRepo.findById(commentId).ifPresent(c -> {
            c.setReported(false);
            commentRepo.saveAndFlush(c);
        });
        if (postId == 0) {
            return "redirect:/admin#comments";
        }
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/comment/delete")
    public String deleteComment(@RequestParam(name = "id") long commentId, @RequestParam(name = "postId") long postId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = 0;
        Optional<Comment> comment = commentRepo.findById(commentId);
        if (comment.isPresent())
            id = comment.get().getUser().getId();
        if (!principal.isAdmin() && principal.getId() != id) {
            return "redirect:/";
        }

        commentRepo.deleteById(commentId);
        if (postId == 0) {
            return "redirect:/admin#comments";
        }
        return "redirect:/posts/" + postId;
    }
}
