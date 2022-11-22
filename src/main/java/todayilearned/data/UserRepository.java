package todayilearned.data;

import org.springframework.data.repository.CrudRepository;
import todayilearned.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
