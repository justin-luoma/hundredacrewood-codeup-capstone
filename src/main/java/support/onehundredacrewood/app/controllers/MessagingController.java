package support.onehundredacrewood.app.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import support.onehundredacrewood.app.dao.models.Message;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.MessageRepo;
import support.onehundredacrewood.app.dao.repositories.UserRepo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Controller
public class MessagingController {
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;

    public MessagingController(MessageRepo messageRepo, UserRepo userRepo) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/messaging")
    @ResponseBody
    public HashMap<String, String> getMessages() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message message = messageRepo.getFirstByReceiverAndPopupOrderByTimestamp(user, false);
        HashMap<String, String> m = new HashMap<>(1);
        if (message == null) {
            m.put("none", "true");
            return m;
        }
        message.setPopup(true);
        messageRepo.saveAndFlush(message);
        m.put("from", message.getSender().getUsername());
        m.put("fromId", String.valueOf(message.getSender().getId()));
        m.put("message", message.getBody());
        return m;
    }

    @PostMapping("/messaging")
    public String sendMessage(
            @RequestParam(name = "body") String body,
            @RequestParam(name = "to") long to) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message message = new Message(
                user,
                userRepo.findById(to),
                LocalDateTime.now(),
                true,
                false,
                body
        );
        messageRepo.saveAndFlush(message);
        return "redirect:/messages";
    }

    @GetMapping("/messaging/count")
    @ResponseBody
    public long countMessages() {
        return messageRepo.countAllByUnread(true);
    }

    @GetMapping("/messages")
    public String showMessages(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Message> messages = messageRepo.findAllByReceiverOrderByTimestampDesc(user);
        model.addAttribute("messages", messages);
        return "messages/messages";
    }
}
