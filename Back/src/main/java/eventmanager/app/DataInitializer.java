package eventmanager.app;

/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import eventmanager.app.entity.User;
import eventmanager.app.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    // Injection du repository via le constructeur
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User user = new User("user@example.com", "user", "user");
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);
        System.out.println("Utilisateur par défaut ajouté : " + user.getEmail());

    }
}
*/