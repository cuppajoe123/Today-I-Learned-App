package todayilearned.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import todayilearned.model.User;
import todayilearned.data.UserRepository;

import java.util.Optional;

@Service
public class UserRepositoryUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepo;

    @Autowired
    public UserRepositoryUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isPresent() && optionalUser.get().isEnabled()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException(
                "User '" + username + "' not found");
    }

}
