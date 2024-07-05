package francescocristiano.U5_W3_D5_Progetto.services;

import francescocristiano.U5_W3_D5_Progetto.entities.User;
import francescocristiano.U5_W3_D5_Progetto.exceptions.UnauthorizedException;
import francescocristiano.U5_W3_D5_Progetto.payloads.UserLoginDTO;
import francescocristiano.U5_W3_D5_Progetto.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserServices userServices;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;


    public String authenticateAndGenerateToken(UserLoginDTO userLoginDTO) {

        User user = userServices.findByUsername(userLoginDTO.username());
        if (bCryptPasswordEncoder.matches(userLoginDTO.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Invalid credentials");
        }
    }
}
