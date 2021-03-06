package support.onehundredacrewood.app.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import support.onehundredacrewood.app.dao.models.Message;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.MessageRepo;
import support.onehundredacrewood.app.dao.repositories.UserRepo;
import support.onehundredacrewood.app.services.NotificationSenderService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Controller
public class MessagingController {
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    private final NotificationSenderService notificationSvc;

    public MessagingController(MessageRepo messageRepo, UserRepo userRepo, NotificationSenderService notificationSvc) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.notificationSvc = notificationSvc;
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
        m.put("id", String.valueOf(message.getId()));
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
                userRepo.findById(to).get(),
                LocalDateTime.now(),
                true,
                false,
                body
        );
        messageRepo.saveAndFlush(message);
        notificationSvc.SendMessage(message);
        return "redirect:/messages";
    }

    @GetMapping("/messaging/count")
    @ResponseBody
    public long countMessages() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return messageRepo.countAllByReceiverAndUnread(principal, true);
    }

    @PostMapping("/messages/read")
    public String markAllRead() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Message> unreadMessages = messageRepo.findAllByUnreadAndReceiver(true, principal);
        unreadMessages.forEach(message -> message.setUnread(false));
        unreadMessages.forEach(m -> System.out.println(m.getUnread()));
        messageRepo.saveAll(unreadMessages);
        messageRepo.flush();
        return "redirect:/messages";
    }

    @GetMapping("/messages")
    public String showMessages(Model model) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findById(principal.getId()).get();
        List<Message> messages = messageRepo.findAllByReceiverOrderByTimestampDesc(principal);
        model.addAttribute("messages", messages);
        model.addAttribute("user", user);
        return "messages/messages";
    }

    @GetMapping("/messages/error")
    public String showError(Model model) {
        model.addAttribute("error", true);
        return "messages/message";
    }

    @GetMapping("/messages/{id}")
    public String showMessage(@PathVariable("id") long id, Model model) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message message = messageRepo.findByReceiverAndId(principal, id);
        if (message == null) {
            return "redirect:/messages/error";
        }
        model.addAttribute("error", false);
        model.addAttribute("message", message);
        return "messages/message";
    }
}
