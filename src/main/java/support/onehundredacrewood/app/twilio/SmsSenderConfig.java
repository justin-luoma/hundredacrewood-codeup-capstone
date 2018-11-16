package support.onehundredacrewood.app.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsSenderConfig {
    @Value("${config.twilio.account-sid}")
    private String twilioAccountSid;
    @Value("${config.twilio.auth-token}")
    private String twilioAuthToken;
    @Value("${config.twilio.phone-number}")
    private String twilioNumber;

    @Bean
    public SmsSender smsSender() {
        return new SmsSender();
    }

    public class SmsSender {
        public void SendSms(String to, String message) {
            Twilio.init(twilioAccountSid, twilioAuthToken);
            PhoneNumber phoneNumber = new PhoneNumber(twilioNumber);

            Message m = Message.creator(
                    new PhoneNumber(to),
                    phoneNumber,
                    message)
                    .create();

//            System.out.println(m.getSid());
        }
    }
}
