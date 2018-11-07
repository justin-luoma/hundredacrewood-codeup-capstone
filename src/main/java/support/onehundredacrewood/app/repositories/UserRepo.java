package support.onehundredacrewood.app.repositories;

import org.springframework.data.repository.CrudRepository;
import support.onehundredacrewood.app.dao.User;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);
}
