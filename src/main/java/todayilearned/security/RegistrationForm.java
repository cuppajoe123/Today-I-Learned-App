package todayilearned.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import todayilearned.User;

@Data
public class RegistrationForm {

    private String email;
    private String username;
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(email, username, passwordEncoder.encode(password));
    }
}
