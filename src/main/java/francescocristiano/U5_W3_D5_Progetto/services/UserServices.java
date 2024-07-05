package francescocristiano.U5_W3_D5_Progetto.services;

import francescocristiano.U5_W3_D5_Progetto.entities.NormalUser;
import francescocristiano.U5_W3_D5_Progetto.entities.Organizer;
import francescocristiano.U5_W3_D5_Progetto.entities.User;
import francescocristiano.U5_W3_D5_Progetto.enums.UserRole;
import francescocristiano.U5_W3_D5_Progetto.exceptions.BadRequestException;
import francescocristiano.U5_W3_D5_Progetto.exceptions.NotFoundExpetion;
import francescocristiano.U5_W3_D5_Progetto.payloads.NewRoleDTO;
import francescocristiano.U5_W3_D5_Progetto.payloads.NewUserDTO;
import francescocristiano.U5_W3_D5_Progetto.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }


    public NormalUser createNormalUser(NewUserDTO newNormalUser) {
        userRepository.findByEmail(newNormalUser.email()).ifPresent(user -> {
            throw new BadRequestException("Email already in use");
        });
        userRepository.findByUsername(newNormalUser.username()).ifPresent(user -> {
            throw new BadRequestException("Username already in use");
        });
        NormalUser normalUser = new NormalUser(newNormalUser.name(), newNormalUser.surname(), newNormalUser.email(), newNormalUser.username(), newNormalUser.password());
        return (NormalUser) saveUser(normalUser);
    }

    public Organizer createOrganizer(NewUserDTO newOrganizer) {
        userRepository.findByEmail(newOrganizer.email()).ifPresent(user -> {
            throw new BadRequestException("Email already in use");
        });
        userRepository.findByUsername(newOrganizer.username()).ifPresent(user -> {
            throw new BadRequestException("Username already in use");
        });
        Organizer organizer = new Organizer(newOrganizer.name(), newOrganizer.surname(), newOrganizer.email(), newOrganizer.username(), newOrganizer.password());
        return (Organizer) saveUser(organizer);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("User not found"));
    }

    public void deleteUserById(UUID id) {
        User foundUser = findById(id);
        if (!foundUser.getBookedEvents().isEmpty()) {
            throw new BadRequestException("User has booked events");
        }
        userRepository.deleteById(id);
    }

    public Page<User> getAllUsers(int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public User findUserAndUpdate(UUID id, NewUserDTO updateUser) {
        User foundUser = findById(id);
        foundUser.setName(updateUser.name());
        foundUser.setSurname(updateUser.surname());
        foundUser.setEmail(updateUser.email());
        foundUser.setUsername(updateUser.username());
        foundUser.setPassword(updateUser.password());
        return userRepository.save(foundUser);
    }

    public User findUserAndUpdateRole(UUID id, NewRoleDTO newRole) {
        User foundUser = findById(id);
        if (!newRole.role().equalsIgnoreCase("USER") && !newRole.role().equalsIgnoreCase("ORGANIZER") && !newRole.role().equalsIgnoreCase("ADMIN")) {
            throw new BadRequestException("Invalid role");
        }
        foundUser.setRole(UserRole.valueOf(newRole.role()));
        return userRepository.save(foundUser);
    }


}
