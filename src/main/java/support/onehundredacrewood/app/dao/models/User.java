package support.onehundredacrewood.app.dao.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 98)
    private String password;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean admin;

    @Column(length = 15)
    private String phone;

    @Column(nullable = false)
    private boolean texts;

    @Column(nullable = false)
    private boolean emails;

    @Column(nullable = false, columnDefinition = "date")
    private LocalDate birthday;

    @Column(length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String gender;

    @Column
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String image;

    @Column
    private Integer strikes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Post> posts;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "friends",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")}
    )
    private List<User> friends;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "follow_post",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id")}
    )
    private List<Post> followedPosts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "follow_topic",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "topic_id")}
    )
    private List<Topic> topics;

    public User() {
    }

    public User(String username, String password, String email, boolean admin
            , String phone, boolean texts, boolean emails, LocalDate birthday
            , String city, String gender, String image, Integer strikes) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.phone = phone;
        this.texts = texts;
        this.emails = emails;
        this.birthday = birthday;
        this.city = city;
        this.gender = gender;
        this.image = image;
        this.strikes = strikes;
    }

    public User(String username, String password, String email, boolean admin
            , String phone, boolean texts, boolean emails, LocalDate birthday
            , String city, String gender, String image, Integer strikes,
                List<Post> posts, List<Comment> comments, List<User> friends,
                List<Post> followedPosts, List<Topic> topics) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.phone = phone;
        this.texts = texts;
        this.emails = emails;
        this.birthday = birthday;
        this.city = city;
        this.gender = gender;
        this.image = image;
        this.strikes = strikes;
        this.posts = posts;
        this.comments = comments;
        this.friends = friends;
        this.followedPosts = followedPosts;
        this.topics = topics;
    }

    public User(User other) {
        this.id = other.id;
        this.username = other.username;
        this.password = other.password;
        this.email = other.email;
        this.admin = other.admin;
        this.phone = other.phone;
        this.texts = other.texts;
        this.emails = other.emails;
        this.birthday = other.birthday;
        this.city = other.city;
        this.gender = other.gender;
        this.image = other.image;
        this.strikes = other.strikes;
        this.posts = other.posts;
        this.comments = other.comments;
        this.friends = other.friends;
        this.followedPosts = other.followedPosts;
        this.topics = other.topics;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getTexts() {
        return texts;
    }

    public void setTexts(boolean texts) {
        this.texts = texts;
    }

    public boolean getEmails() {
        return emails;
    }

    public void setEmails(boolean emails) {
        this.emails = emails;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStrikes() {
        return strikes;
    }

    public void setStrikes(Integer strikes) {
        this.strikes = strikes;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<Post> getFollowedPosts() {
        return followedPosts;
    }

    public void setFollowedPosts(List<Post> followedPosts) {
        this.followedPosts = followedPosts;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}
