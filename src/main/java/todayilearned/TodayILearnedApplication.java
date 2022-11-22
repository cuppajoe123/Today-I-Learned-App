package todayilearned;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;

import java.util.Date;

@SpringBootApplication
public class TodayILearnedApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodayILearnedApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(SubmissionRepository submissionRepo, UserRepository userRepo) {
		return args -> {
			User joe = new User("jstrauss24@bfhsla.org", "cuppajoe", "password");
			userRepo.save(joe);
			submissionRepo.deleteAll();
			submissionRepo.save(new Submission(0L, joe, new Date(), "Title: Foobar", "Body: Bar of Foo"));
			submissionRepo.save(new Submission(1L, joe, new Date(), "Title: Foobar", "Body: Bar of Foo"));
			submissionRepo.save(new Submission(2L, joe, new Date(), "Title: Foobar", "Body: Bar of Foo"));
			submissionRepo.save(new Submission(3L, joe, new Date(), "Title: Foobar", "Body: Bar of Foo"));
			submissionRepo.save(new Submission(4L, joe, new Date(), "Title: Foobar", "Body: Bar of Foo"));
			submissionRepo.save(new Submission(5L, joe, new Date(), "Title: Foobar", "Body: Bar of Foo"));
		};
	}

}
