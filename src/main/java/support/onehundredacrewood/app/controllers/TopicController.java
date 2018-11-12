package support.onehundredacrewood.app.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import support.onehundredacrewood.app.dao.repositories.TopicRepo;

@Controller
public class TopicController {

    private final TopicRepo topicRepo;

    public TopicController(TopicRepo topicRepo) {
        this.topicRepo = topicRepo;
    }

    @GetMapping("/topic")
    public String showTopics(Model model) {
        model.addAttribute("topic",topicRepo.findAll());
        return "topics/topics";
    }

    @GetMapping("/topic/{id}")
    public String individualTopic(@PathVariable long id, Model model) {
//        model.addAttribute("topics", topicRepo.findById(id));
        model.addAttribute("id", id);

        return "topics/topic";
    }


}