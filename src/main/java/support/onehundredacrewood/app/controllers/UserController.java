package support.onehundredacrewood.app.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserController {

    @GetMapping("/profile")
    public String showProfile() {
        return "profile";
    }



}