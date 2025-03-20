package eventmanager.app.service;
import eventmanager.app.entity.User;
import eventmanager.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import eventmanager.app.entity.Event;
import eventmanager.app.repository.EventRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Fetch events by creator email
    public List<Event> getEventsByCreatorEmail(String email) {
        return eventRepository.findByCreatorEmail(email);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Transactional
    public void registerUserToEvent(Long eventId, String mail) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Optional<User> userOptional = userRepository.findByEmail(mail);

        if (eventOptional.isPresent() && userOptional.isPresent()) {
            Event event = eventOptional.get();
            User user = userOptional.get();

            if (!user.getEventIds().contains(eventId)) {
                user.getEventIds().add(eventId);
                userRepository.save(user);
            }
        }
    }

    @Transactional
    public void unregisterUserFromEvent(Long eventId, String mail) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Optional<User> userOptional = userRepository.findByEmail(mail);

        if (eventOptional.isPresent() && userOptional.isPresent()) {
            Event event = eventOptional.get();
            User user = userOptional.get();

            if (user.getEventIds().contains(eventId)) {
                user.getEventIds().remove(eventId);
                userRepository.save(user);
            }
        }
    }

    public Event editEvent(Long eventId, Event updatedEvent) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found")); // Vérifie que l'event existe

        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();


        if (!existingEvent.getCreatorEmail().equals(loggedInUser)) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à modifier cet événement");
        }


        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setDate(updatedEvent.getDate());
        existingEvent.setLocation(updatedEvent.getLocation());

        return eventRepository.save(existingEvent);
    }


    public void deleteEvent(Long eventId, String userEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getCreatorEmail().equals(userEmail)) {
            throw new IllegalArgumentException("You can only delete your own events.");
        }

        eventRepository.delete(event);
    }







    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
