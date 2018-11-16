package support.onehundredacrewood.app.mailgun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailSenderConfig {
    @Value("${config.mailgun.api-key}")
    private String mailgunApiKey;
    @Value("${config.mailgun.base-url}")
    private String mailgunBaseUrl;

    @Bean
    public EmailSender emailSender() {
        return new EmailSender();
    }

    public class EmailSender {
        public void sendSimpleMessage(String to, String subject, String message) {
            try {
                HttpResponse<JsonNode> request = Unirest.post(mailgunBaseUrl + "/messages")
                        .basicAuth("api", mailgunApiKey)
                        .queryString("from", "<noreply@hundredacrewood.support>")
                        .queryString("to", to)
                        .queryString("subject", subject)
                        .queryString("text", message)
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
//            return request.getBody();
        }
    }
}
