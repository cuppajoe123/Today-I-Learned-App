package todayilearned.data;

import org.springframework.data.jpa.repository.JpaRepository;
import todayilearned.model.VerificationToken;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
