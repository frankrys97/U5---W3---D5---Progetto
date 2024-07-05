package francescocristiano.U5_W3_D5_Progetto.controllers;


import francescocristiano.U5_W3_D5_Progetto.entities.Event;
import francescocristiano.U5_W3_D5_Progetto.entities.User;
import francescocristiano.U5_W3_D5_Progetto.exceptions.BadRequestException;
import francescocristiano.U5_W3_D5_Progetto.payloads.NewRoleDTO;
import francescocristiano.U5_W3_D5_Progetto.payloads.NewUserDTO;
import francescocristiano.U5_W3_D5_Progetto.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserServices userServices;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return userServices.getAllUsers(page, size, sortBy);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUserById(@PathVariable("id") UUID id) {
        userServices.deleteUserById(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findById(@PathVariable("id") UUID id) {
        return userServices.findById(id);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateRole(@PathVariable UUID id, @RequestBody @Validated NewRoleDTO role, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return userServices.findUserAndUpdateRole(id, role);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUser(@PathVariable UUID id, @RequestBody @Validated NewUserDTO user, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return userServices.findUserAndUpdate(id, user);
    }


    @GetMapping("/me")
    public User getCurrentAuthenticatedUser(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @GetMapping("/me/events")
    public List<Event> getMyEvents(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return currentAuthenticatedUser.getBookedEvents();
    }

    @DeleteMapping("/me")
    public void deleteMyAccount(@AuthenticationPrincipal User currentAuthenticatedUser) {
        userServices.deleteUserById(currentAuthenticatedUser.getId());
    }

    @PutMapping("/me")
    public User updateMyAccount(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated NewUserDTO user, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return userServices.findUserAndUpdate(currentAuthenticatedUser.getId(), user);
    }
}
