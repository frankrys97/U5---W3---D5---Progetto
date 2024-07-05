package francescocristiano.U5_W3_D5_Progetto.controllers;

import francescocristiano.U5_W3_D5_Progetto.entities.Event;
import francescocristiano.U5_W3_D5_Progetto.entities.User;
import francescocristiano.U5_W3_D5_Progetto.payloads.NewEventDTO;
import francescocristiano.U5_W3_D5_Progetto.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public Event createEvent(@RequestBody @Validated NewEventDTO newEvent, @AuthenticationPrincipal User user) {
        String organizerEmail = user.getEmail();
        newEvent.organizersEmails().add(organizerEmail);
        return eventService.createEvent(newEvent);
    }
}
