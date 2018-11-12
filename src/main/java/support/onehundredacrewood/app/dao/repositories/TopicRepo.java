package support.onehundredacrewood.app.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import support.onehundredacrewood.app.dao.models.Topic;

public interface TopicRepo extends CrudRepository<Topic, Long> {
    Topic findById(long id);
}
