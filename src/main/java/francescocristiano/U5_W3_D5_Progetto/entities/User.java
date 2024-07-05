package francescocristiano.U5_W3_D5_Progetto.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import francescocristiano.U5_W3_D5_Progetto.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@NoArgsConstructor
@JsonIgnoreProperties({"password", "role", "authorities", "enabled", "accountNonExpired", "credentialsNonExpired", "accountNonLocked"})
public abstract class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;
    @Setter
    private String name;
    @Setter
    private String surname;
    @Setter
    private String email;
    @Setter
    private String username;
    @Setter
    private String password;

    @Setter
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @JsonBackReference
    @ManyToMany(mappedBy = "partecipants")
    private List<Event> bookedEvents;

    public User(String name, String surname, String email, String username, String password, UserRole role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
