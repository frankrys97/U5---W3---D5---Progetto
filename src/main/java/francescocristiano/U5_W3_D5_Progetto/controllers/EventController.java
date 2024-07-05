package francescocristiano.U5_W3_D5_Progetto.controllers;

import francescocristiano.U5_W3_D5_Progetto.entities.Event;
import francescocristiano.U5_W3_D5_Progetto.entities.User;
import francescocristiano.U5_W3_D5_Progetto.enums.UserRole;
import francescocristiano.U5_W3_D5_Progetto.exceptions.UnauthorizedException;
import francescocristiano.U5_W3_D5_Progetto.payloads.NewCancelUserDTO;
import francescocristiano.U5_W3_D5_Progetto.payloads.NewEventDTO;
import francescocristiano.U5_W3_D5_Progetto.services.EventService;
import francescocristiano.U5_W3_D5_Progetto.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserServices userServices;


    @GetMapping("/{eventId}")
    public Event getEvent(@PathVariable UUID eventId) {
        return eventService.findById(eventId);
    }


    @GetMapping
    public Page<Event> getAllEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return eventService.getAllEvents(page, size, sortBy);
    }


    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable UUID eventId, @AuthenticationPrincipal User user) {
        if (user.getRole().equals(UserRole.ADMIN)) {
            eventService.deleteEvent(eventId);
        } else {
            Event foundEvent = eventService.findById(eventId);
            if (foundEvent.getOrganizers().stream().noneMatch(organizer -> organizer.getEmail().equals(user.getEmail()))) {
                throw new UnauthorizedException("You are not the organizer of this event");
            }
            eventService.deleteEvent(eventId);
        }
        ;
    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public Event createEvent(@RequestBody @Validated NewEventDTO newEvent, @AuthenticationPrincipal User user) {
        String organizerEmail = user.getEmail();
        newEvent.organizersEmails().add(organizerEmail);
        return eventService.createEvent(newEvent);
    }

    @PatchMapping("/me/{eventId}")
    public Event addUserToEvent(@PathVariable UUID eventId, @AuthenticationPrincipal User user) {
        return eventService.addUserToEvent(eventId, user);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public Event updateEvent(@PathVariable UUID eventId, @RequestBody @Validated NewEventDTO newEvent, @AuthenticationPrincipal User user) {
        if (user.getRole().equals(UserRole.ADMIN) || eventService.findById(eventId).getOrganizers().stream().anyMatch(organizer -> organizer.getEmail().equals(user.getEmail()))) {
            return eventService.updateEvent(eventId, newEvent);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @PatchMapping("/{eventId}/cancelation")
    public Event removeUserFromEvent(
            @PathVariable UUID eventId,
            @AuthenticationPrincipal User user,
            @RequestBody @Validated NewCancelUserDTO newCancelUserDTO) {

        Event event = eventService.findById(eventId);
        User foundUser = userServices.findById(newCancelUserDTO.id());

        if (user.getRole() == UserRole.ADMIN || event.getOrganizers().stream().anyMatch(organizer -> organizer.getEmail().equals(user.getEmail()))) {
            return eventService.removeUserFromEvent(eventId, foundUser);
        } else if (user.getRole() == UserRole.USER) {
            if (event.getPartecipants().stream().anyMatch(participant -> participant.getId().equals(user.getId()))) {
                return eventService.removeUserFromEvent(eventId, foundUser);
            } else {
                throw new UnauthorizedException("You are not booked for this event");
            }
        } else {
            throw new UnauthorizedException("Unauthorized access");
        }
    }


}
