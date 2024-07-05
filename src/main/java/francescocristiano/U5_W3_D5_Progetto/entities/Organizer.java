package francescocristiano.U5_W3_D5_Progetto.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import francescocristiano.U5_W3_D5_Progetto.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class Organizer extends User {


    @ManyToMany(mappedBy = "organizers")
    @JsonBackReference
    private List<Event> organizedEvents;


    public Organizer(String name, String surname, String email, String username, String password) {
        super(name, surname, email, username, password, UserRole.ORGANIZER);
    }

}
