package todayilearned.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import todayilearned.data.TokenRepository;
import todayilearned.model.User;
import todayilearned.model.VerificationToken;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private MessageSource messages;

    @Autowired
    EmailService emailService;

    @Autowired
    private TokenRepository tokenRepo;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        final VerificationToken verificationToken = new VerificationToken(token, user);
        tokenRepo.save(verificationToken);

        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLocale());

        emailService.sendSimpleMessage(user.getEmail(), subject, message + "\r\n" + "http://localhost:8080" + confirmationUrl);
    }
}
