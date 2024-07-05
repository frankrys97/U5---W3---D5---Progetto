package francescocristiano.U5_W3_D5_Progetto.payloads;

import jakarta.validation.constraints.NotBlank;

public record NewRoleDTO(
        @NotBlank
        String role) {
}
