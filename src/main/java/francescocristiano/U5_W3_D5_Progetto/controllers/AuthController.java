package francescocristiano.U5_W3_D5_Progetto.controllers;

import francescocristiano.U5_W3_D5_Progetto.entities.User;
import francescocristiano.U5_W3_D5_Progetto.exceptions.BadRequestException;
import francescocristiano.U5_W3_D5_Progetto.payloads.NewUserDTO;
import francescocristiano.U5_W3_D5_Progetto.payloads.UserLoginDTO;
import francescocristiano.U5_W3_D5_Progetto.payloads.UserLoginResponseDTO;
import francescocristiano.U5_W3_D5_Progetto.services.AuthService;
import francescocristiano.U5_W3_D5_Progetto.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserServices userServices;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody @Validated UserLoginDTO userLoginDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return new UserLoginResponseDTO(authService.authenticateAndGenerateToken(userLoginDTO));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Validated NewUserDTO newUserDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return userServices.createNormalUser(newUserDTO);
    }

    @PostMapping("/register/organizer")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerOrganizer(@RequestBody @Validated NewUserDTO newUserDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return userServices.createOrganizer(newUserDTO);
    }
}
