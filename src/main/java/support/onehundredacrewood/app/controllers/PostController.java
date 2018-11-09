package support.onehundredacrewood.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import support.onehundredacrewood.app.dao.models.Post;
import support.onehundredacrewood.app.dao.repositories.PostRepo;

@Controller
public class PostController {
    private final PostRepo postRepo;

    public PostController(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    @GetMapping("/posts")
    public String getPost(Model model) {
        Post post = postRepo.findById(1);
        model.addAttribute("post", post);
        return "post/one";
    }
}
