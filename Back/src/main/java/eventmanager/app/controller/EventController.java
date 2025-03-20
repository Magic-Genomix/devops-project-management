package eventmanager.app.controller;

import eventmanager.app.repository.UserRepository;
import eventmanager.app.service.UserDetails;
import eventmanager.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import eventmanager.app.entity.Event;
import eventmanager.app.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;


    @Autowired
    private UserService service;

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody Event event, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String creatorEmail = userDetails.getUsername();
        event.setCreatorEmail(creatorEmail);

        Event createdEvent = eventService.createEvent(event);

        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId, @AuthenticationPrincipal UserDetails userDetails) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!event.getCreatorEmail().equals(userDetails.getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(event);
    }

    // Get events by user email
    @GetMapping("/myevents")
    public ResponseEntity<List<Event>> getEventsByUserEmail(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String creatorEmail = userDetails.getUsername();

        List<Event> events = eventService.getEventsByCreatorEmail(creatorEmail);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/{eventId}/register")
    public String registerUserToEvent(@PathVariable Long eventId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "non autorise";
        }

        String email = userDetails.getUsername();
        eventService.registerUserToEvent(eventId, email);
        return "User registered successfully";
    }

    @PostMapping("/{eventId}/unregister")
    public ResponseEntity<String> unregisterUserFromEvent(@PathVariable Long eventId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED); // Retourne 401 Unauthorized
        }

        String email = userDetails.getUsername();
        eventService.unregisterUserFromEvent(eventId, email);
        return new ResponseEntity<>("User unregistered successfully", HttpStatus.OK);
    }

    @GetMapping("/registredEvents")
    public ResponseEntity<List<Long>> getEventIdsByUserEmail(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = userDetails.getUsername();
        List<Long> eventIds = service.getRegistredEventIds(email);
        return new ResponseEntity<>(eventIds, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Event>> listEvents() {
        List<Event> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Event> editEvent(@PathVariable Long eventId, @RequestBody Event updatedEvent, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {

            Event updatedEventFromService = eventService.editEvent(eventId, updatedEvent);
            return ResponseEntity.ok(updatedEventFromService);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED); // Retourne 401 Unauthorized
        }

        String email = userDetails.getUsername();

        try {
            eventService.deleteEvent(eventId, email);
            return new ResponseEntity<>("Event deleted successfully", HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
