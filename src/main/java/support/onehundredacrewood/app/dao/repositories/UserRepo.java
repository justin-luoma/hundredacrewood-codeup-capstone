package support.onehundredacrewood.app.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import support.onehundredacrewood.app.dao.models.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

    List<User> findAllByStrikesGreaterThan(int strikes);
}
