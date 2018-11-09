package support.onehundredacrewood.app.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import support.onehundredacrewood.app.dao.models.Post;

public interface PostRepo extends CrudRepository<Post, Long> {
    Post findById(long id);
}
