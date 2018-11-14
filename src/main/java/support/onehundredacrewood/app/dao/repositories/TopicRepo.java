package support.onehundredacrewood.app.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import support.onehundredacrewood.app.dao.models.Topic;

import java.util.List;

public interface TopicRepo extends CrudRepository<Topic, Long> {

    List<Topic> findAll();

    Topic findById(long id);
}
