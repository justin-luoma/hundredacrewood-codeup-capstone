package support.onehundredacrewood.app.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String getPost(Model model) {
        Post post = postRepo.findById(7);
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
}
