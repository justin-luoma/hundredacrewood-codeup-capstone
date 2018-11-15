package support.onehundredacrewood.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.models.UserWithRoles;
import support.onehundredacrewood.app.dao.repositories.UserRepo;
import support.onehundredacrewood.app.security.Argon2PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"registering","provider"})
public class OAuthLoginController {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private boolean registering = false;
    private String provider;

    public OAuthLoginController(UserRepo userRepo,
                                PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/oauth2/register")
    public String oauthRegister(@ModelAttribute User user,
                                Model model,
                                @RequestParam(name = "birthdayString") String birthday,
                                @RequestParam(name = "genderString") String gender) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Username already exists");
            return "oauth/register";
        }
        user.setGender(gender);
        LocalDate birth = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        user.setBirthday(birth);
        user.setOauthLogin(true);
        user.setOauthProvider(provider);
        user.setPassword(passwordEncoder.encode(user.getUsername()));
        if (user.getImage().isEmpty())
            user.setImage(null);
        User newUser = userRepo.saveAndFlush(user);
        refreshLogin(newUser);
        return "redirect:/profile";
    }

    @PostMapping("/oauth2/registering")
    public String oauthRegisterRedirect(@RequestParam(name = "provider") String provider) {
        registering = true;
        return "redirect:/oauth2/authorization/" + provider;
    }

    @GetMapping("/oauth2/success")
    public String oauthSuccess(Principal principal, HttpSession session, Model model) {
        provider = ((OAuth2AuthenticationToken) principal).getAuthorizedClientRegistrationId();
        String email = (String) ((OAuth2AuthenticationToken) principal).getPrincipal().getAttributes().get("email");
        User user = userRepo.findByEmail(email);
        if (registering) {
            if (user != null) {
                registering = false;
                session.invalidate();
                return "redirect:/register?error=Email already exists";
            }
            User newUser = new User();
            newUser.setEmail(email);
            model.addAttribute("user", newUser);
            registering = false;
            return "oauth/register";
        }
//        String email = (String)((OAuth2User) principal).getAttributes().get("email");
        if (user != null && user.isOauthLogin() && user.getOauthProvider().equals(provider)) {
//            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//            authorities.add(new SimpleGrantedAuthority("USER"));
//            if (user.isAdmin())
//                authorities.add(new SimpleGrantedAuthority("ADMIN"));
//            UserWithRoles userWithRoles = new UserWithRoles(user);
//            Authentication auth = new UsernamePasswordAuthenticationToken(userWithRoles, "oauth", authorities);
//            SecurityContextHolder.getContext().setAuthentication(auth);
            refreshLogin(user);
            return "redirect:/";
        }
        session.invalidate();
        return "redirect:/login?oauth";
    }

    private void refreshLogin(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        if (user.isAdmin())
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        UserWithRoles userWithRoles = new UserWithRoles(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userWithRoles, "", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}