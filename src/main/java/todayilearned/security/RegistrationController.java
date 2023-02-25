package todayilearned.security;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import todayilearned.data.TokenRepository;
import todayilearned.data.UserRepository;
import todayilearned.model.User;
import todayilearned.model.VerificationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepo;
    private final TokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messages;

    public RegistrationController(UserRepository userRepo, TokenRepository tokenRepo, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher, MessageSource messages) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.messages = messages;
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

        String appUrl = request.getRequestURI();
        User user = form.toUser(passwordEncoder);
        userRepo.save(user);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl, request.getLocale()));

        request.login(form.getUsername(), form.getPassword());
        return "redirect:/";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(HttpServletRequest request, @RequestParam("token") String token, Model model) {
        Locale locale = request.getLocale();

        Optional<VerificationToken> verificationTokenOptional = tokenRepo.findByToken(token);
        VerificationToken verificationToken;
        if (verificationTokenOptional.isEmpty()) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        } else
            verificationToken = verificationTokenOptional.get();


        User user = verificationToken.getUser();
        Duration duration = Duration.between(LocalDateTime.now(), verificationToken.getExpiryDate());
        long minutes = duration.toMinutes();
        if (minutes > 60 * 24 || minutes < 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "redirect:/badUser";
        }

        user.setEnabled(true);
        userRepo.save(user);
        return "redirect:/login";
    }
}
