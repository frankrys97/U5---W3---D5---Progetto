package francescocristiano.U5_W3_D5_Progetto.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime date;
    private String location;
    private int maxCapacity;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "event_partecipants", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> partecipants;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "event_organizers", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "organizer_id"))
    private List<User> organizers;


    public Event(String name, String description, LocalDateTime date, String location, int maxCapacity, List<User> organizers) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.organizers = organizers;
    }
}
