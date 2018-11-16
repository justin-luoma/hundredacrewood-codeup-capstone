package support.onehundredacrewood.app.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.UserRepo;

import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
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
    public String register(@ModelAttribute User user, @RequestParam(name = "birthdayString") String birthday, @RequestParam(name = "genderString") String gender) {
        LocalDate birth = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        user.setBirthday(birth);
        user.setGender(gender);

        if (userRepo.findByUsername(user.getUsername()) != null) {
            user.setPassword(null);
            return "redirect:/register?error=Username already exists";
        } else if (userRepo.findByEmail(user.getEmail()) != null) {
            user.setPassword(null);
            return "redirect:/register?error=Email already exists";
        }
        if (user.getImage().isEmpty())
            user.setImage(null);
        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "redirect:/login";
    }
}
