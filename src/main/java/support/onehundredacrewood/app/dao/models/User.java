package support.onehundredacrewood.app.dao.models;

import javax.persistence.*;
import java.time.LocalDate;

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

    public User(User copy) {
        this.id = copy.id;
        this.username = copy.username;
        this.password = copy.password;
        this.email = copy.email;
        this.admin = copy.admin;
        this.phone = copy.phone;
        this.texts = copy.texts;
        this.emails = copy.emails;
        this.birthday = copy.birthday;
        this.city = copy.city;
        this.gender = copy.gender;
        this.image = copy.image;
        this.strikes = copy.strikes;
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

    public boolean isAdmin() {
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

    public boolean isTexts() {
        return texts;
    }

    public void setTexts(boolean texts) {
        this.texts = texts;
    }

    public boolean isEmails() {
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
}
