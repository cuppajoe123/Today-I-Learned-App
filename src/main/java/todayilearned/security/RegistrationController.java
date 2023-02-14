package todayilearned.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import todayilearned.data.UserRepository;
import todayilearned.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registrationForm";
    }

    @PostMapping
    public String processRegistration(HttpServletRequest request, @ModelAttribute @Valid RegistrationForm form, Errors errors) throws ServletException {
        if (errors.hasErrors()) {
            return "registrationForm";
        }
        Optional<User> userOptional = userRepo.findByEmail(form.getEmail());
        if (userOptional.isPresent()) {
            errors.reject("email", "Email already in use");
            return "registrationForm";
        }

        userOptional = userRepo.findByUsername(form.getUsername());
        if (userOptional.isPresent()) {
            errors.reject("username", "Username already in use");
            return "registrationForm";
        }

        userRepo.save(form.toUser(passwordEncoder));
        request.login(form.getUsername(), form.getPassword());
        return "redirect:/";
    }
}
