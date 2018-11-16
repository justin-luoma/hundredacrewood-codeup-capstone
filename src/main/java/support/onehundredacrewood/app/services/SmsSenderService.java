package support.onehundredacrewood.app.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import support.onehundredacrewood.app.dao.models.Message;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.twilio.SmsSenderConfig;

@Service
public class SmsSenderService {
    private final SmsSenderConfig.SmsSender smsSender;

    public SmsSenderService(SmsSenderConfig.SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    @Async
    public void SendMessage(Message message) {
        User user = message.getReceiver();
        if (user.isTexts() && user.getPhone() != null && !user.getPhone().isEmpty()) {
            String m = message.getBody().length() < 140 ? message.getBody() : message.getBody().substring(0, 100) + "..." +
                    "\nmessage truncated, read the rest @ hundredacrewood.support";
            smsSender.SendSms(user.getPhone(),
                    "New message from: " + message.getSender().getUsername() + "\n" + m +
                            "\nHundredAcreWood.support");
        }
    }
}
