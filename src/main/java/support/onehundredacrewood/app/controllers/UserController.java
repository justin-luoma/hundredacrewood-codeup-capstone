package support.onehundredacrewood.app.controllers;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import support.onehundredacrewood.app.dao.models.Post;
import support.onehundredacrewood.app.dao.models.Topic;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.PostRepo;
import support.onehundredacrewood.app.dao.repositories.TopicRepo;
import support.onehundredacrewood.app.dao.repositories.UserRepo;

import java.util.Comparator;


@Controller
public class UserController {

    private final UserRepo userRepo;
    private final TopicRepo topicRepo;
    private final PostRepo postRepo;

    public UserController(UserRepo userRepo,TopicRepo topicRepo,PostRepo postRepo) {
        this.userRepo = userRepo;
        this.topicRepo = topicRepo;
        this.postRepo = postRepo;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        User principal = ( User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(principal.getId());
        user.getPosts().sort(Comparator.comparing(Post::getCreated).reversed());

        model.addAttribute("user",user);
        model.addAttribute("post", postRepo.getTop3ByUserOrderByCreatedDesc(user));
        return "profile";
    }

    @GetMapping("/users/{id}")
    public String userProfiles(@PathVariable(name = "id") long id, Model model){
        User user = userRepo.findById(id);
        model.addAttribute("topic", topicRepo.findById(id));

        model.addAttribute("user", user);
        return "users/user";
    }

    @PostMapping("/users/follow")
    public String followUser(@RequestParam(name = "id") long id){
        User principal = ( User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(principal.getId());
        User friend = userRepo.findById(id);

        user.addFriend(friend);
        userRepo.save(user);

        return "redirect:/users/" + id;
    }

}