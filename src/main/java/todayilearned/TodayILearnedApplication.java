package todayilearned;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TodayILearnedApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodayILearnedApplication.class, args);
	}
}
