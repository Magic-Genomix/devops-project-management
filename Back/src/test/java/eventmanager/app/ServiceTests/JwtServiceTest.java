package eventmanager.app.ServiceTests;


import eventmanager.app.service.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
    }

    @Test
    void testGenerateToken() {
        String userName = "testUser";

        String token = jwtService.generateToken(userName);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));  // Un JWT commence par "eyJ"
    }

    @Test
    void testExtractUsername() {
        String userName = "testUser";
        String token = jwtService.generateToken(userName);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(userName, extractedUsername);
    }

    @Test
    void testExtractExpiration() {
        String userName = "testUser";
        String token = jwtService.generateToken(userName);

        Date expirationDate = jwtService.extractExpiration(token);

        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));  // L'expiration doit être dans le futur
    }

    @Test
    void testValidateToken_Valid() {
        String userName = "testUser";
        String token = jwtService.generateToken(userName);

        when(userDetails.getUsername()).thenReturn(userName);

        Boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUsername() {
        String validUserName = "testUser";
        String invalidUserName = "invalidUser";
        String token = jwtService.generateToken(validUserName);

        when(userDetails.getUsername()).thenReturn(invalidUserName);

        Boolean isValid = jwtService.validateToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void testValidateToken_Expired() {
        String userName = "testUser";
        String token = jwtService.generateToken(userName);

        Boolean isValid = jwtService.validateToken(token, userDetails);

        // Asserts que le token est invalide parce qu'il est expiré
        assertFalse(isValid, "Le token devrait être invalide car il est expiré");
    }
}
