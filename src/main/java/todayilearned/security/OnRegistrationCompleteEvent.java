package todayilearned.security;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import todayilearned.model.User;

import java.util.Locale;

@Data
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    private String appUrl;
    private Locale locale;

    public OnRegistrationCompleteEvent(User user, String appUrl, Locale locale) {
        super(user);

        this.user = user;
        this.appUrl = appUrl;
        this.locale = locale;
    }
}
