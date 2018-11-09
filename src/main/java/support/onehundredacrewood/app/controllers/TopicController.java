package support.onehundredacrewood.app.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopicController {

    @GetMapping("/topic")
    public String showTopics(){
        return "topic";
    }
}
