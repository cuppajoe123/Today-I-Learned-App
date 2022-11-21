package todayilearned;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import todayilearned.data.SubmissionRepository;

import java.util.Date;

@SpringBootApplication
public class TodayILearnedApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodayILearnedApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(SubmissionRepository repo) {
		return args -> {
			repo.deleteAll();
			repo.save(new Submission(0L, new Date(), "Title: Foobar", "Body: Bar of Foo"));
			repo.save(new Submission(1L, new Date(), "Title: Foobar", "Body: Bar of Foo"));
			repo.save(new Submission(2L, new Date(), "Title: Foobar", "Body: Bar of Foo"));
			repo.save(new Submission(3L, new Date(), "Title: Foobar", "Body: Bar of Foo"));
			repo.save(new Submission(4L, new Date(), "Title: Foobar", "Body: Bar of Foo"));
			repo.save(new Submission(5L, new Date(), "Title: Foobar", "Body: Bar of Foo"));
		};
	}

}
