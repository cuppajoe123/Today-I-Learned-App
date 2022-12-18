package todayilearned;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.util.HtmlService;

import java.sql.SQLException;
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
            final Date date = new Date();
            userRepo.save(joe);
            userRepo.save(linus);
            submissionRepo.deleteAll();
            submissionRepo.save(new Submission(joe, date, "Title: a", body + 'a', htmlService.markdownToHtml(body + 'a')));
            submissionRepo.save(new Submission(linus, date, "Title: b", body + 'b', htmlService.markdownToHtml(body + 'b')));
            submissionRepo.save(new Submission(joe, date, "Title: d", body + 'c', htmlService.markdownToHtml(body + 'c')));
            submissionRepo.save(new Submission(joe, date, "Title: e", body + 'd', htmlService.markdownToHtml(body + 'd')));
            submissionRepo.save(new Submission(joe, date, "Title: f", body + 'e', htmlService.markdownToHtml(body + 'e')));
            for (int i = 0; i < 105; i++) {
                submissionRepo.save(new Submission(joe, date, i + ". This will most likely be the average length of a title", body, htmlService.markdownToHtml(body)));
            }
        };
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
}
