package todayilearned;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.security.RegistrationForm;

import java.util.Date;

@SpringBootApplication
public class TodayILearnedApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodayILearnedApplication.class, args);
	}
}
