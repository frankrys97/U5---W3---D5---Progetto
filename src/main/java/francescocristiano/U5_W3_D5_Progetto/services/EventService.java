package francescocristiano.U5_W3_D5_Progetto.services;

import francescocristiano.U5_W3_D5_Progetto.entities.Event;
import francescocristiano.U5_W3_D5_Progetto.entities.User;
import francescocristiano.U5_W3_D5_Progetto.exceptions.NotFoundExpetion;
import francescocristiano.U5_W3_D5_Progetto.payloads.NewEventDTO;
import francescocristiano.U5_W3_D5_Progetto.repositories.EventRepository;
import francescocristiano.U5_W3_D5_Progetto.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    public Event findById(UUID id) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Event not found"));
    }

    public Event addUserToEvent(UUID id, User user) {
        Event event = findById(id);
        event.getPartecipants().add(user);
        eventRepository.save(event);
        return event;
    }

    public Page<Event> getAllEvents(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventRepository.findAll(pageable);
    }

    public void deleteEvent(UUID id) {
        eventRepository.deleteById(id);
    }

}
