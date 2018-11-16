package support.onehundredacrewood.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import support.onehundredacrewood.app.dao.repositories.CommentRepo;
import support.onehundredacrewood.app.dao.repositories.PostRepo;
import support.onehundredacrewood.app.dao.repositories.UserRepo;
import support.onehundredacrewood.app.twilio.SmsSenderConfig;

@Controller
public class AdminController {
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final SmsSenderConfig.SmsSender smsSender;

    public AdminController(PostRepo postRepo, CommentRepo commentRepo,
                           UserRepo userRepo, SmsSenderConfig.SmsSender smsSender) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.smsSender = smsSender;
    }

    @GetMapping("/test")
    @ResponseBody
    public String testsms() {
        smsSender.SendSms("2182599534", "hello");
        return "test";
    }

    @GetMapping("/admin")
    public String showAdmin(Model model) {
        model.addAttribute("reportedPosts", postRepo.findAllByReportedAndLockedOrderByCreatedDesc(true, false));
        model.addAttribute("reportedComments", commentRepo.findAllByReportedOrderByCreatedDesc(true));
        model.addAttribute("usersWithStrikes", userRepo.findAllByStrikesGreaterThan(0));
        return "admin/main";
    }
}
