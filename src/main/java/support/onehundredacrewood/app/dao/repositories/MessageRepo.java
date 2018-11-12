package support.onehundredacrewood.app.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import support.onehundredacrewood.app.dao.models.Message;
import support.onehundredacrewood.app.dao.models.User;

import java.util.List;


public interface MessageRepo extends JpaRepository<Message, Long> {
    Message getFirstByReceiverAndPopupOrderByTimestamp(User user, boolean popup);
    long countAllByReceiverAndUnread(User receiver, boolean unread);
    List<Message> findAllByReceiverOrderByTimestampDesc(User user);
    Message findByReceiverAndId(User user, long id);
    List<Message> findAllByUnreadAndReceiver(boolean unread, User receiver);
}
