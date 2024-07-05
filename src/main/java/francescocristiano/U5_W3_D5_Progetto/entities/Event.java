package francescocristiano.U5_W3_D5_Progetto.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private int capacity;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "event_partecipants", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> partecipants;

    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "event_organizers", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "organizer_id"))
    private List<Organizer> organizers;


    public Event(String name, String description, LocalDateTime date, String location, int capacity, List<Organizer> organizers) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
        this.organizers = organizers;
    }
}
