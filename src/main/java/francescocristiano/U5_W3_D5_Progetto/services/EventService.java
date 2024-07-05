package francescocristiano.U5_W3_D5_Progetto.services;

import francescocristiano.U5_W3_D5_Progetto.entities.Event;
import francescocristiano.U5_W3_D5_Progetto.entities.User;
import francescocristiano.U5_W3_D5_Progetto.exceptions.BadRequestException;
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

import java.util.Iterator;
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
        if (event.getPartecipants().contains(user)) {
            throw new BadRequestException("User already partecipant of this event");
        } else if (event.getPartecipants().size() >= event.getMaxCapacity()) {
            throw new BadRequestException("Event is full");
        } else {
            event.getPartecipants().add(user);
            return eventRepository.save(event);
        }
    }

    public Page<Event> getAllEvents(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventRepository.findAll(pageable);
    }

    public void deleteEvent(UUID id) {
        eventRepository.deleteById(id);
    }

    public Event updateEvent(UUID id, NewEventDTO newEvent) {
        Event event = findById(id);
        event.setName(newEvent.name());
        event.setDescription(newEvent.description());
        event.setDate(newEvent.date());
        event.setLocation(newEvent.location());
        event.setMaxCapacity(newEvent.maxCapacity());
        return eventRepository.save(event);
    }

    public Event removeUserFromEvent(UUID id, User user) {
        Event event = findById(id);
        Iterator<User> iterator = event.getPartecipants().iterator();
        while (iterator.hasNext()) {
            User participant = iterator.next();
            if (participant.equals(user)) {
                iterator.remove();
            }
        }
        return eventRepository.save(event);
    }

}
