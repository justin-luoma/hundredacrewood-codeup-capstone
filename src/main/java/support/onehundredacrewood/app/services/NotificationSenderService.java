package support.onehundredacrewood.app.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import support.onehundredacrewood.app.dao.models.Message;
import support.onehundredacrewood.app.dao.models.Post;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.mailgun.EmailSenderConfig;
import support.onehundredacrewood.app.twilio.SmsSenderConfig;

import java.util.List;

@Service
public class NotificationSenderService {
    private final String url = "https://hundredacrewood.support";
    private final SmsSenderConfig.SmsSender smsSender;
    private final EmailSenderConfig.EmailSender emailSender;

    public NotificationSenderService(SmsSenderConfig.SmsSender smsSender,
                                     EmailSenderConfig.EmailSender emailSender) {
        this.smsSender = smsSender;
        this.emailSender = emailSender;
    }

    @Async
    public void ProcessPostAction(Post post, boolean isComment, Long commenterId) {
        List<User> usersFollowingPost = post.getUsersFollowing();
        usersFollowingPost.forEach(u -> {
            if (!isComment || u.getId() != commenterId)
                FollowPostUpdate(u, post, isComment);
        });
    }

    @Async
    public void SendMessage(Message message) {
        User user = message.getReceiver();
        if (allowSendSms(user)) {
            String m = message.getBody().length() < 140 ? message.getBody() : message.getBody().substring(0, 100) + "..." +
                    "\nmessage truncated, read the rest @ hundredacrewood.support";
            smsSender.SendSms(user.getPhone(),
                    "New message from: " + message.getSender().getUsername() + "\n" + m +
                            "\n\nHundredAcreWood.support");
        }
    }

    @Async
    public void FollowUserUpdate(User messageRecipient, User user, Post post, boolean isComment) {
        if (allowSendSms(messageRecipient)) {
            String to = messageRecipient.getPhone();
            String followingUser = user.getUsername();
            String link = url + "/posts/" + post.getId();
            String verb = isComment ? " commented on a post " : " created a new post ";
            String message = followingUser + verb + "to view visit: " + link + "\n\nHundredAcreWood.support";
            smsSender.SendSms(to, message);
        }
    }

    @Async
    public void FollowPostUpdate(User messageRecipient, Post post, boolean isComment) {
        String followingPost = post.getTitle();
        String link = url + "/posts/" + post.getId();
        String what = isComment ? " was commented on " : " was updated ";
        if (allowSendSms(messageRecipient)) {
            String to = messageRecipient.getPhone();
            String message = "A post you are following" + what + "to view visit: " + link + "\n\nHundredAcreWood.support";
            smsSender.SendSms(to, message);
        }
        if (allowSendEmail(messageRecipient)) {
            String to = messageRecipient.getEmail();
            String subject = "Post: " + followingPost + " updated.";
            String message = "A post you are following" + what + "to view visit: " + link + "\n\nHundredAcreWood.support";
            emailSender.sendSimpleMessage(to, subject, message);
        }
    }

    private boolean allowSendSms(User user) {
        return user.isTexts() && user.getPhone() != null && !user.getPhone().isEmpty();
    }

    private boolean allowSendEmail(User user) {
        return user.isEmails() && user.getEmail() != null && !user.getEmail().isEmpty();
    }
}
