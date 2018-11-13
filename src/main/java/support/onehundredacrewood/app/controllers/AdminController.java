package support.onehundredacrewood.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import support.onehundredacrewood.app.dao.repositories.CommentRepo;
import support.onehundredacrewood.app.dao.repositories.PostRepo;
import support.onehundredacrewood.app.dao.repositories.UserRepo;

@Controller
public class AdminController {
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final UserRepo userRepo;

    public AdminController(PostRepo postRepo, CommentRepo commentRepo,
                           UserRepo userRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/admin")
    public String showAdmin(Model model) {
        model.addAttribute("posts", postRepo.findAllByReportedAndLockedOrderByCreatedDesc(true, false));
        model.addAttribute("comments", commentRepo.findAllByReportedOrderByCreatedDesc(true));
        model.addAttribute("users", userRepo.findAllByStrikesGreaterThan(0));
        return "admin/main";
    }
}
