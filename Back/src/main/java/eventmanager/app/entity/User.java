package eventmanager.app.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private String password;

    @ElementCollection
    @CollectionTable(name = "user_events", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "event_id")
    private List<Long> eventIds = new ArrayList<>(); // Initialize the list

    // Constructor with parameters
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.eventIds = new ArrayList<>();
    }
}
