package support.onehundredacrewood.app.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import support.onehundredacrewood.app.dao.models.Post;

import java.util.List;

public interface PostRepo extends CrudRepository<Post, Long> {
    Post findById(long id);

    List<Post> getAllByIdExistsOrderByCreatedDesc();
}