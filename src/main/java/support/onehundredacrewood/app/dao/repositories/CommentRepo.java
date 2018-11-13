package support.onehundredacrewood.app.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import support.onehundredacrewood.app.dao.models.Comment;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findAllByReportedOrderByCreatedDesc(boolean reported);
}
