package todayilearned.data;

import org.springframework.data.repository.CrudRepository;
import todayilearned.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
