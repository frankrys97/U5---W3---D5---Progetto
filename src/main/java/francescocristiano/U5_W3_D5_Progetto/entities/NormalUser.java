package francescocristiano.U5_W3_D5_Progetto.entities;

import francescocristiano.U5_W3_D5_Progetto.enums.UserRole;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class NormalUser extends User {

    public NormalUser(String name, String surname, String email, String username, String password) {
        super(name, surname, email, username, password, UserRole.USER);
    }
}
