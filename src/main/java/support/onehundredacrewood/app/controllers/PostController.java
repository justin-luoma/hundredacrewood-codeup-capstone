package support.onehundredacrewood.app.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import support.onehundredacrewood.app.dao.models.Post;
import support.onehundredacrewood.app.dao.models.Topic;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.PostRepo;
import support.onehundredacrewood.app.dao.repositories.TopicRepo;
import support.onehundredacrewood.app.dao.repositories.UserRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class PostController {
    private final PostRepo postRepo;
    private final TopicRepo topicRepo;
    private final UserRepo userRepo;

    public PostController(PostRepo postRepo, TopicRepo topicRepo,
                          UserRepo userRepo) {
        this.postRepo = postRepo;
        this.topicRepo = topicRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/posts")
    public String showPosts(Model vModel) {
        vModel.addAttribute("posts", postRepo.findAll());
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


        model.addAttribute("post", post);
        model.addAttribute("isFollowing", isFollowing);
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
        Post newPost = postRepo.save(post);
        System.out.println(Arrays.toString(topics));
        return "redirect:/posts/"+ newPost.getId();
    }

    @GetMapping("/posts/{id}/update")
    public String editPost(@PathVariable long id, Model vModel) {
        vModel.addAttribute("post", postRepo.findById(id));
        return "post/edit";
    }

    @PostMapping("/posts/{id}/update")
    public String updatePost(@PathVariable long id, @ModelAttribute Post post) {
        Post postUpdate = postRepo.findById(id);
        postUpdate.setTitle(post.getTitle());
        postUpdate.setBody(post.getBody());
        postRepo.save(postUpdate);
        return "redirect:/posts/"+ post.getId();
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable long id) {
        postRepo.deleteById(id);
        return "redirect:/posts";
    }

    @GetMapping("/posts/myposts")
    public String showUserPosts( Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("posts", postRepo.findAllByUserOrderByCreatedDesc(user));
        return "post/index";
    }

    @PostMapping("posts/follow")
    public String followPosts(@RequestParam(name = "id") long id, @RequestHeader(value = "referer") final String referer) {
        System.out.println(referer);
        User principal = ( User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepo.findById(id);
        User user = userRepo.findById(principal.getId()).get();


        user.followPost(post);
        postRepo.save(post);

        return "redirect:" + referer;
    }
}
