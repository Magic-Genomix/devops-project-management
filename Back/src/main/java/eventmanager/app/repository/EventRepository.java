package eventmanager.app.repository;

import eventmanager.app.entity.Event;
import eventmanager.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findById(Long id);

    List<Event> findByCreatorEmail(String creatorEmail);



}
