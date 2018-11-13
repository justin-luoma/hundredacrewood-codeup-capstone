package support.onehundredacrewood.app.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import support.onehundredacrewood.app.dao.models.Post;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.PostRepo;

import java.time.LocalDateTime;

@Controller
public class PostController {
    private final PostRepo postRepo;

    public PostController(PostRepo postRepo) {

        this.postRepo = postRepo;
    }

    @GetMapping("/posts")
    public String showPosts(Model vModel) {
        vModel.addAttribute("posts", postRepo.findAll());
        return "post/index";
    }

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable long id, Model model) {
        Post post = postRepo.findById(id);
        model.addAttribute("post", post);
        return "post/one";
    }

    @GetMapping("/posts/create")
    public String createPostForm(Model vModel) {
        vModel.addAttribute("post", new Post());
        return "post/create";
    }

    @PostMapping("/posts/create")
    public String createPost(@ModelAttribute Post post) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUser(user);
        post.setCreated(LocalDateTime.now());
        post.setReported(false);
        post.setLocked(false);
        postRepo.save(post);
        return "redirect:/post/"+ post.getId();
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
}
