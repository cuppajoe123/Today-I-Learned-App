package todayilearned.security;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import todayilearned.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegistrationForm {

    @Email(message = "A valid email address is required")
    private String email;

    @NotBlank(message = "A username is required")
    private String username;
    @NotBlank(message = "A password is required")
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(email, username, passwordEncoder.encode(password));
    }
}
