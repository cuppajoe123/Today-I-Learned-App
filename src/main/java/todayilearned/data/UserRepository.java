package todayilearned.data;

import org.springframework.data.repository.CrudRepository;
import todayilearned.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
