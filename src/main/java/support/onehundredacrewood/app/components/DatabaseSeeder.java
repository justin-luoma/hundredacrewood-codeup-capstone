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
                    new Topic("Depression", "You say you're depressed - all I see is resilience. You are allowed to feel messed up and inside out. It doesn't mean you're defective, it just means you're human. - David Mitchell"),
                    new Topic("Anxiety", "I wanted a perfect ending. Now I've learned, the hard way, that some poems don't rhyme, and some stories don't have a clear beginning, middle, and end. Life is about not knowing, having to change, taking the moment and making the best of it, without knowing what's going to happen next. - Gilda Radner"),
                    new Topic("Death", "If there ever comes a day when we can't be together keep me in your heart, I'll stay there forever. - A.A. Milne"),
                    new Topic("Chronic Pain", "Everyone you meet is fighting a battle you know nothing about. Be kind, always. - unknown"),
                    new Topic("Addiction", "It is not cocaine or heroin that makes one an addict, it is the need to escape from a harsh reality. - Shirley Chisholm"),
                    new Topic("Eating Disorder", "The truth is: belonging starts with self-acceptance. Your level of belonging, in fact, can never be greater than your level of self-acceptance, because believing that you're enough is what gives you the courage to be authentic, vulnerable, and imperfect. - Brene Brown"),
                    new Topic("Gay & Lesbian", "It's almost like I could feel you holding your breath. But you get to exhale now, you get to be more you than you've been in a very long time. - Becky Albertalli"),
                    new Topic("Family", "If I'd known the freedom in letting go, I would have never held on. - Rachel Firmin"),
                    new Topic("Divorce", "You're looking for the explanation, the loophole, the bright twist in the dark tale that reverses your story's course. But it won't reverse-for me or for you or for anyone who has ever been wronged, which is everyone. Allow your acceptance of the universality of suffering to be a transformative experience. You do that by simply looking at what pains you squarely in the face and then moving on. You don't have to move fast or far. You can go just an inch, you can mark your progress breath by breath. - Cheryl Strayed"),
                    new Topic("Love", "They will never know the memories you carry with you, or the ones you have left behind. - Isra Al-Thibeh"),
                    new Topic("Postpartum Depression", "When you study postpartum depression, there is a very clear understanding that in communities where you see more support, there is less depression. - Ariel Gore")
            };
            topicRepo.saveAll(Arrays.asList(topics));
        }
    }
}
