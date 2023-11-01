package dev.vimlesh.userservicetestfinal.services;

import dev.vimlesh.userservicetestfinal.dtos.SignUpResponseDto;
import dev.vimlesh.userservicetestfinal.dtos.UserDto;
import dev.vimlesh.userservicetestfinal.models.Session;
import dev.vimlesh.userservicetestfinal.models.SessionStatus;
import dev.vimlesh.userservicetestfinal.models.User;
import dev.vimlesh.userservicetestfinal.repositories.SessionRepository;
import dev.vimlesh.userservicetestfinal.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.vimlesh.userservicetestfinal.utils.serviceUtilityMethods.getJWTToken;
import static dev.vimlesh.userservicetestfinal.utils.serviceUtilityMethods.isValidEmail;

@Service
public class AuthService {


    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<UserDto> login(String email, String password) {
        if(isValidEmail(email)){
            throw new RuntimeException("Please enter a valid email address");
        }
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("something went wrong!");
        }

        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong username or password");
        }


        String token = getJWTToken(user);
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = UserDto.from(user);

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);

        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            throw new RuntimeException("something went wrong");
        }

        Session session = sessionOptional.get();

        session.setSessionStatus(SessionStatus.ENDED);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }

    public SignUpResponseDto signUp(String userName, String email, String password) {
        if(isValidEmail(email)){
            throw new RuntimeException("Please enter a valid email address");
        }
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        signUpResponseDto.setUserName(savedUser.getUserName());
        return signUpResponseDto;
    }

    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty() || sessionOptional.get().getSessionStatus().equals(SessionStatus.ENDED)) {
           //if session is ended log out the user
            this.logout(token, userId);
            return SessionStatus.ENDED;
        }
         return SessionStatus.ACTIVE;
    }


}
