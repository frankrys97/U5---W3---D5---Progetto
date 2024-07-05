package francescocristiano.U5_W3_D5_Progetto.services;

import francescocristiano.U5_W3_D5_Progetto.entities.Event;
import francescocristiano.U5_W3_D5_Progetto.entities.User;
import francescocristiano.U5_W3_D5_Progetto.payloads.NewEventDTO;
import francescocristiano.U5_W3_D5_Progetto.repositories.EventRepository;
import francescocristiano.U5_W3_D5_Progetto.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public Event createEvent(NewEventDTO newEvent) {
        List<User> organizers = newEvent.organizersEmails().stream().map(email -> userRepository.findByEmail(email).orElse(null)).filter(Objects::nonNull).toList();
        Event event = new Event(newEvent.name(), newEvent.description(), newEvent.date(), newEvent.location(), newEvent.maxCapacity(), organizers);
        return eventRepository.save(event);
    }
}
