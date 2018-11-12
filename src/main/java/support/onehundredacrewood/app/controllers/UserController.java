package support.onehundredacrewood.app.controllers;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.TopicRepo;
import support.onehundredacrewood.app.dao.repositories.UserRepo;


@Controller
public class UserController {

    private final UserRepo userRepo;
    private final TopicRepo topicRepo;

    public UserController(UserRepo userRepo,TopicRepo topicRepo) {
        this.userRepo = userRepo;
        this.topicRepo = topicRepo;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        User principal = ( User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(principal.getId());

        model.addAttribute("user",user);
        return "profile";
    }

    @GetMapping("/users/{id}")
    public String userProfiles(@PathVariable(name = "id") long id, Model model){
        User user = userRepo.findById(id);
        model.addAttribute("topic", topicRepo.findById(id));

        model.addAttribute("user", user);
        return "users/user";
    }


}