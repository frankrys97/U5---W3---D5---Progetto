package francescocristiano.U5_W3_D5_Progetto.payloads;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank
        String username,
        @NotBlank
        String password) {
}
