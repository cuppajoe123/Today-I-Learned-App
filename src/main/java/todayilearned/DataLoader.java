package todayilearned;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.util.HtmlService;

import java.util.Date;

@Configuration
public class DataLoader {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HtmlService htmlService;

    @Bean
    public CommandLineRunner loadData(SubmissionRepository submissionRepo, UserRepository userRepo) {
        return args -> {
            User joe = new User("jstrauss24@bfhsla.org", "cuppajoe", passwordEncoder.encode("password"));
            User linus = new User("linus@kernel.org", "linus", passwordEncoder.encode("password"));
            final String body = "Body: ";
            userRepo.save(joe);
            userRepo.save(linus);
            submissionRepo.deleteAll();
            submissionRepo.save(new Submission(joe, new Date(), "Title: a", body + 'a', htmlService.markdownToHtml(body + 'a')));
            submissionRepo.save(new Submission(linus, new Date(), "Title: b", body + 'b', htmlService.markdownToHtml(body + 'b')));
            submissionRepo.save(new Submission(joe, new Date(), "Title: d", body + 'c', htmlService.markdownToHtml(body + 'c')));
            submissionRepo.save(new Submission(joe, new Date(), "Title: e", body + 'd', htmlService.markdownToHtml(body + 'd')));
            submissionRepo.save(new Submission(joe, new Date(), "Title: f", body + 'e', htmlService.markdownToHtml(body + 'e')));
        };
    }
}
