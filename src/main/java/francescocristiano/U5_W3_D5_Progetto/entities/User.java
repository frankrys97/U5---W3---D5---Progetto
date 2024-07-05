package francescocristiano.U5_W3_D5_Progetto.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import francescocristiano.U5_W3_D5_Progetto.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@NoArgsConstructor
public abstract class User {
    @Id
    @GeneratedValue
    private UUID id;
    @Setter
    private String name;
    @Setter
    private String surname;
    @Setter
    private String email;
    @Setter
    private String username;
    @Setter
    private String password;

    @Setter
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @JsonBackReference
    @ManyToMany(mappedBy = "partecipants")
    private List<Event> bookedEvents;

    public User(String name, String surname, String email, String username, String password, UserRole role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
