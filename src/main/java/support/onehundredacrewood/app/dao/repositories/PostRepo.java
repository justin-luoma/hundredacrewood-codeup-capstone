package support.onehundredacrewood.app.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import support.onehundredacrewood.app.dao.models.Post;
import support.onehundredacrewood.app.dao.models.Topic;

import java.util.List;

public interface PostRepo extends CrudRepository<Post, Long> {
    Post findById(long id);

    @Query(nativeQuery = true, value = "select * from posts order by created desc limit 3")
    List<Post> getTop3OrderByCreatedDesc();

    List<Post> getTop3ByTopicsOrderByCreatedDesc(Topic topics);
}
