package todayilearned;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;

import java.util.Date;

@SpringBootApplication
@Slf4j
public class TodayILearnedApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodayILearnedApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(SubmissionRepository submissionRepo, UserRepository userRepo) {
		return args -> {
			User joe = new User("jstrauss24@bfhsla.org", "cuppajoe", "password");
			User linus = new User("linus@kernel.org", "linus", "password");
			userRepo.save(joe);
			userRepo.save(linus);
			submissionRepo.deleteAll();
			submissionRepo.save(new Submission(joe, new Date(), "Title: a", "Body: a"));
			log.info("User: {}", linus);
			log.info("Submission: {}, ", new Submission(linus, new Date(), "Title: b", "Body: b"));
			log.info("Submission: {}", submissionRepo.save(new Submission(linus, new Date(), "Title: b", "Body: b")));
			log.info("Submission: {}", submissionRepo.findByAuthor(linus).toString());
			/* THE BUG IS AN ID COLLISION */
			submissionRepo.save(new Submission(linus, new Date(), "Title: c", "Body: c"));
			submissionRepo.save(new Submission(joe, new Date(), "Title: d", "Body: d"));
			submissionRepo.save(new Submission(joe, new Date(), "Title: e", "Body: e"));
			submissionRepo.save(new Submission(joe, new Date(), "Title: f", "Body: f"));
		};
	}

}
