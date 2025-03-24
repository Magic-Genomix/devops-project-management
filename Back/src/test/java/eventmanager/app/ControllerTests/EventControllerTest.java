package eventmanager.app.ControllerTests;


import eventmanager.app.controller.EventController;
import eventmanager.app.entity.Event;
import eventmanager.app.entity.User;
import eventmanager.app.service.EventService;
import eventmanager.app.service.JwtService;
import eventmanager.app.service.UserDetails;
import eventmanager.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private JwtService jwtService;


    @InjectMocks
    private EventController eventController;

    private Event event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Format as needed, e.g., yyyy-MM-dd
        String dateString = formatter.format(currentDate);
        event.setDate(dateString);
        event.setLocation("Test Location");
        event.setCreatorEmail("testuser@example.com");
        jwtService = new JwtService();

    }


    @Test
    void testAddNewUser() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn("User Added Successfully");

        mockMvc.perform(post("/auth/addNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"testuser@example.com\",\"password\":\"newpassword\",\"name\":\"New User\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User Added Successfully"));
    }
    @Test
    void testCreateEvent() throws Exception {
        String userName = "testuser@example.com";

        String token = jwtService.generateToken(userName);

        System.out.println(token);
        when(eventService.createEvent(any(Event.class))).thenReturn(event);

        mockMvc.perform(post("/api/events/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Test Event\", \"description\": \"Test Description\", \"date\": \"2026-03-21\", \"location\": \"Test Location\" }")
                        .header("Authorization", "Bearer " + token))  // Pass the token in the header
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }



    @Test
    void testGetEventForbidden() throws Exception {
        when(eventService.getEventById(1L)).thenReturn(event);
        when(userDetails.getUsername()).thenReturn("wronguser@example.com");

        mockMvc.perform(get("/api/events/{eventId}", 1L)
                        .principal(() -> "wronguser@example.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEventsByUserEmail() throws Exception {
        List<Event> events = new ArrayList<>();
        String userName = "testuser@example.com";

        String token = jwtService.generateToken(userName);
        events.add(event);
        when(userDetails.getUsername()).thenReturn("testuser@example.com");
        when(eventService.getEventsByCreatorEmail("testuser@example.com")).thenReturn(events);

        mockMvc.perform(get("/api/events/myevents")
                        .header("Authorization", "Bearer " + token)  // Add token in the header
                        .principal(() -> "testuser@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Event"));
    }

    @Test
    void testRegisterUserToEvent() throws Exception {
        String userName = "testuser@example.com";

        String token = jwtService.generateToken(userName);
        mockMvc.perform(post("/api/events/{eventId}/register", 1L)
                        .header("Authorization", "Bearer " + token)  // Add token in the header
                        .principal(() -> "testuser@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testUnregisterUserFromEvent() throws Exception {
        String userName = "testuser@example.com";

        String token = jwtService.generateToken(userName);
        mockMvc.perform(post("/api/events/{eventId}/unregister", 1L)
                        .header("Authorization", "Bearer " + token)  // Add token in the header
                        .principal(() -> "testuser@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("User unregistered successfully"));
    }





}
