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
            smsSender.SendSms(user.getPhone(),
                    constructMessage(message.getSender().getUsername(), message.getBody()));
        }
    }

    private static String constructMessage(String from, String body) {
        return "New message from: " + from + "\n" + body +
                "\nHundredAcreWood.support";
    }
}
