package support.onehundredacrewood.app.controllers;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import support.onehundredacrewood.app.dao.models.Post;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.PostRepo;
import support.onehundredacrewood.app.dao.repositories.TopicRepo;
import support.onehundredacrewood.app.dao.repositories.UserRepo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


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
        User user = userRepo.findById(principal.getId()).get();
        user.getPosts().sort(Comparator.comparing(Post::getCreated).reversed());

        model.addAttribute("user",user);
        model.addAttribute("post", postRepo.getTop3ByUserOrderByCreatedDesc(user));
        return "profile";
    }

    @GetMapping("/users/{id}")
    public String userProfiles(@PathVariable(name = "id") long id, Model model){
        Optional<User> oUser = userRepo.findById(id);
        if (!oUser.isPresent()) {
            return "redirect:/";
        }
        User user = oUser.get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean friends = false;
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated()) {
            User principal = ( User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User me = userRepo.findById(principal.getId()).get();
            friends = me.getId() == user.getId() || areFriends(me.getFriends(), user.getId());
        }


        model.addAttribute("user", user);
        model.addAttribute("isFriend", friends);
        return "users/user";
    }

    @PostMapping("/users/follow")
    public String followUser(@RequestParam(name = "id") long id){
        User principal = ( User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(principal.getId()).get();
        User friend = userRepo.findById(id).get();

        user.addFriend(friend);
        userRepo.save(user);

        return "redirect:/users/" + id;
    }

    @PostMapping("/users/toggle-disabled")
    public String disableUserToggle(@RequestParam(name = "id") long userId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principal.isAdmin()) {
            return "redirect:/";
        }

        User user = userRepo.findById(userId).get();
        user.setDisabled(!user.isDisabled());
        userRepo.saveAndFlush(user);

        return "redirect:/users/" + userId;
    }

    private boolean areFriends(List<User> friends, final long userId) {
        return  friends.stream().anyMatch(f -> f.getId() == userId);
    }
}