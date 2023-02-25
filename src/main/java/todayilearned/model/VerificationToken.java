package todayilearned.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Entity
@Data
@NoArgsConstructor(access= AccessLevel.PRIVATE, force = true)
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate = LocalDateTime.from(LocalDateTime.now().plus(1, ChronoUnit.DAYS));

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
