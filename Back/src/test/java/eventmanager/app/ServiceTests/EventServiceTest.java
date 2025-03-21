package eventmanager.app.ServiceTests;

import eventmanager.app.entity.Event;
import eventmanager.app.entity.User;
import eventmanager.app.repository.EventRepository;
import eventmanager.app.repository.UserRepository;
import eventmanager.app.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        Date currentDate = new Date(); // Get the current date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Format as needed, e.g., yyyy-MM-dd
        String dateString = formatter.format(currentDate);
        event.setDate(dateString);
        event.setLocation("Test Location");
        event.setCreatorEmail("creator@example.com");

        user = new User();
        user.setEmail("user@example.com");
    }

    @Test
    void testCreateEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(event);

        assertNotNull(createdEvent);
        assertEquals("Test Event", createdEvent.getTitle());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testGetEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event foundEvent = eventService.getEventById(1L);

        assertNotNull(foundEvent);
        assertEquals("Test Event", foundEvent.getTitle());
    }

    @Test
    void testGetEventByIdEventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.getEventById(1L);
        });

        assertEquals("Event not found", exception.getMessage());
    }

    @Test
    void testRegisterUserToEvent() {
        // Mock the event and user repository responses
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // Call the method under test
        eventService.registerUserToEvent(1L, "user@example.com");

        // Assert that the user now has the event ID in their list of event IDs
        assertTrue(user.getEventIds().contains(1L));

        // Verify that the user repository's save method was called exactly once with the updated user
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void testUnregisterUserFromEvent() {
        user.getEventIds().add(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        eventService.unregisterUserFromEvent(1L, "user@example.com");

        assertFalse(user.getEventIds().contains(1L));
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void testEditEvent() {
        // Prepare the updated event
        Event updatedEvent = new Event();
        updatedEvent.setTitle("Updated Event");
        updatedEvent.setDescription("Updated Description");
        Date currentDate = new Date(); // Get the current date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Format as needed, e.g., yyyy-MM-dd
        String dateString = formatter.format(currentDate);
        updatedEvent.setDate(dateString);
        updatedEvent.setLocation("Updated Location");

        // Mock the SecurityContext and Authentication to simulate the logged-in user
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getName()).thenReturn("creator@example.com"); // Simulate the logged-in user being the creator
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        // Set up the event repository mock
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        // Call the method under test
        Event editedEvent = eventService.editEvent(1L, updatedEvent);

        // Assert the expected result
        assertEquals("Updated Event", editedEvent.getTitle());
        assertEquals("Updated Description", editedEvent.getDescription());
        assertEquals("Updated Location", editedEvent.getLocation());

        // Verify that save was called once with the updated event
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository, times(1)).save(eventCaptor.capture());
        Event capturedEvent = eventCaptor.getValue();

        assertEquals("Updated Event", capturedEvent.getTitle());
        assertEquals("Updated Description", capturedEvent.getDescription());
        assertEquals("Updated Location", capturedEvent.getLocation());

        // Ensure the event creator email is the same as the logged-in user
        assertEquals("creator@example.com", capturedEvent.getCreatorEmail());
    }

    @Test
    void testEditEventNotCreator() {
        // Prepare the updated event
        Event updatedEvent = new Event();
        updatedEvent.setTitle("Updated Event");
        updatedEvent.setDescription("Updated Description");
        updatedEvent.setDate("2025-03-21");
        updatedEvent.setLocation("Updated Location");

        // Mock the SecurityContext and Authentication to simulate a non-creator user
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getName()).thenReturn("user@example.com"); // Simulate the logged-in user NOT being the creator
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        // Set up the event repository mock
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // Try to edit the event and expect an exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.editEvent(1L, updatedEvent);
        });

        assertEquals("Vous n'êtes pas autorisé à modifier cet événement", exception.getMessage());
    }


    @Test
    void testDeleteEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        eventService.deleteEvent(1L, "creator@example.com");

        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    void testDeleteEventNotCreator() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.deleteEvent(1L, "user@example.com");
        });

        assertEquals("You can only delete your own events.", exception.getMessage());
    }

    @Test
    void testGetAllEvents() {
        List<Event> events = new ArrayList<>();
        events.add(event);
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> allEvents = eventService.getAllEvents();

        assertNotNull(allEvents);
        assertEquals(1, allEvents.size());
        assertEquals("Test Event", allEvents.get(0).getTitle());
    }
}
