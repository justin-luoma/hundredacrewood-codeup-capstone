package support.onehundredacrewood.app.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import support.onehundredacrewood.app.dao.models.Topic;
import support.onehundredacrewood.app.dao.repositories.PostRepo;
import support.onehundredacrewood.app.dao.repositories.TopicRepo;

@Controller
public class TopicController {

    private final TopicRepo topicRepo;
    private final PostRepo postRepo;

    public TopicController(TopicRepo topicRepo,PostRepo postRepo) {
        this.topicRepo = topicRepo;
        this.postRepo = postRepo;
    }

    @GetMapping("/topic")
    public String showTopics(Model model) {
        model.addAttribute("topic",topicRepo.findAll());
        return "topics/topics";
    }

    @GetMapping("/topic/{id}")
    public String individualTopic(@PathVariable long id, Model model) {
        Topic topic = topicRepo.findById(id);
        model.addAttribute("topic", topic);
        model.addAttribute("posts", postRepo.getTop3ByTopicsOrderByCreatedDesc(topic));

        return "topics/topic";
    }

}