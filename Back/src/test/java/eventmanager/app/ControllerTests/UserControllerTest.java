package eventmanager.app.ControllerTests;

import eventmanager.app.controller.UserController;
import eventmanager.app.entity.AuthRequest;
import eventmanager.app.entity.User;
import eventmanager.app.service.JwtService;
import eventmanager.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;




    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    private User user;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("testuser@example.com", "password", "Test User");
        authRequest = new AuthRequest("testuser@example.com", "password");
        jwtService = new JwtService();
        userService.addUser(new User("testuser3", "password456", "testuser3@example.com"));


    }

    @Test
    void testWelcome() throws Exception {
        mockMvc.perform(get("/auth/welcome"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome this endpoint is not secure"));
    }

    @Test
    void testAddNewUser() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn("User Added Successfully");

        mockMvc.perform(post("/auth/addNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"newuser3@example.com\",\"password\":\"newpassword\",\"name\":\"New User\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User Added Successfully"));
    }


    @Test
    void testAddNewUserEmailAlreadyInUse() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn("Email already in use");

        mockMvc.perform(post("/auth/addNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"newuser3@example.com\",\"password\":\"password\",\"name\":\"Test User\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already in use"));
    }



    @Test
    void testDeleteUser() throws Exception {
        // Simulating that the user exists in the database and will be deleted
        when(userService.deleteUser("newuser3@example.com")).thenReturn("User deleted successfully");

        // Perform the DELETE request
        mockMvc.perform(delete("/auth/deleteUser/newuser3@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Expect 200 OK status code
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }





}
