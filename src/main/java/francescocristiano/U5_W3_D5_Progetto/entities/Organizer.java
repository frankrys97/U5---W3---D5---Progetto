package francescocristiano.U5_W3_D5_Progetto.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import francescocristiano.U5_W3_D5_Progetto.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class Organizer extends User {

    @ManyToMany(mappedBy = "organizers")
    @JsonManagedReference
    private List<Event> organizedEvents;

    public Organizer(String name, String surname, String email, String username, String password) {
        super(name, surname, email, username, password, UserRole.ORGANIZER);
    }

}
