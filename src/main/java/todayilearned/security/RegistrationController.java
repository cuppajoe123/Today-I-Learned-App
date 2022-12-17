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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
//        model.addAttribute("email", "");
//        model.addAttribute("username", "");
//        model.addAttribute("password", "");
//        model.addAttribute("confirm", "");
        return "registrationForm";
    }

    @PostMapping
    public String processRegistration(HttpServletRequest request, @ModelAttribute @Valid RegistrationForm form, BindingResult bindingResult, Model model) throws ServletException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("email", form.getEmail());
            model.addAttribute("username", form.getUsername());
            model.addAttribute("password", form.getPassword());
            model.addAttribute("confirm", form.getPassword());
            return "registrationForm";
        }
        userRepo.save(form.toUser(passwordEncoder));
        request.login(form.getUsername(), form.getPassword());
        return "redirect:/";
    }
}
