package support.onehundredacrewood.app.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import support.onehundredacrewood.app.dao.models.User;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);
}
