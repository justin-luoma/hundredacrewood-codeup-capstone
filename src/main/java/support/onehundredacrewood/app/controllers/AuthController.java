package support.onehundredacrewood.app.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.UserRepo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class AuthController {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, @RequestParam(name = "birthdayString") String birthday) {
        LocalDate birth = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        user.setBirthday(birth);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "redirect:/login";
    }
}
