package francescocristiano.U5_W3_D5_Progetto.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record NewEventDTO(

        @NotBlank
        String name,
        @NotBlank
        String description,
        @Future
        LocalDateTime date,
        @NotBlank
        String location,
        @NotNull
        int maxCapacity,
        List<String> organizersEmails) {
}
