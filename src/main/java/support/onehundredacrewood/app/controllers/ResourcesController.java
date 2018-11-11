package support.onehundredacrewood.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResourcesController {
    @GetMapping("/resources")
    public String showResources() {
        return "resources";
    }
}