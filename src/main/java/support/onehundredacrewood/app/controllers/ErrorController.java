package support.onehundredacrewood.app.controllers;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;

@Controller
public class ErrorController {
    @GetMapping("/403")
    @ResponseBody
    public Map<String, Object> error403(OAuth2AuthenticationToken principal) {
        OAuth2User oAuth2User = principal.getPrincipal();
        return oAuth2User.getAttributes();
//        Map<String, Object> test = (Map<String, Object>) principal;
//        System.out.println(test.get("principal"));

//        return "test";
    }
}
