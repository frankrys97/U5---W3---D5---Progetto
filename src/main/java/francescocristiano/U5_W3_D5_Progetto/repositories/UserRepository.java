package francescocristiano.U5_W3_D5_Progetto.repositories;

import francescocristiano.U5_W3_D5_Progetto.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}