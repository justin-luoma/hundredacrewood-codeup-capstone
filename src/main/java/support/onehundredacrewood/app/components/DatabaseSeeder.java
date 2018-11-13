package support.onehundredacrewood.app.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import support.onehundredacrewood.app.dao.models.Topic;
import support.onehundredacrewood.app.dao.models.User;
import support.onehundredacrewood.app.dao.repositories.TopicRepo;
import support.onehundredacrewood.app.dao.repositories.UserRepo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.StreamSupport;

//DatabaseSeeder will seed the database with data if none exists
@Component
public class DatabaseSeeder {
    private final UserRepo userRepo;
    private final TopicRepo topicRepo;

    //application.properties config.isDev={boolean}
    @Value("${config.isDev}")
    private boolean dev;

    @Autowired
    public DatabaseSeeder(UserRepo userRepo, TopicRepo topicRepo) {
        this.userRepo = userRepo;
        this.topicRepo = topicRepo;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedTopics();
        if (dev) {
            seedUsers();
        }
    }

    //create an admin user admin:admin if isDev and none exists
    private void seedUsers() {
        long count = StreamSupport.stream(
                userRepo.findAll().spliterator(),
                false)
                .count();
        if (count < 1) {
            userRepo.save(new User(
                    "admin",
                    "$argon2id$v=19$m=65536,t=25,p=4$6+dxi6WogIu52AbVLUoXyg$TZf3ZN5AeK89+KnI+gGzDv8j3mOZSEt4sMvXX5wUJ2I",
                    "admin@mail.com",
                    true,
                    "1111111111",
                    false,
                    false,
                    LocalDate.now(),
                    null,
                    "other",
                    null,
                    null
            ));
        }
    }

    private void seedTopics() {
        long count = StreamSupport.stream(
                topicRepo.findAll().spliterator(),
                false)
                .count();
        if (count < 11) {
            Topic topics[] = {
                    new Topic("Depression", "TBD"),
                    new Topic("Anxiety", "TBD"),
                    new Topic("Death", "TBD"),
                    new Topic("Chronic Pain", "TBD"),
                    new Topic("Addiction", "TBD"),
                    new Topic("Eating Disorder", "TBD"),
                    new Topic("Gay & Lesbian", "TBD"),
                    new Topic("Family", "TBD"),
                    new Topic("Divorce", "TBD"),
                    new Topic("Love", "TBD"),
                    new Topic("Postpartum Depression", "TBD")
            };
            topicRepo.saveAll(Arrays.asList(topics));
        }
    }
}
