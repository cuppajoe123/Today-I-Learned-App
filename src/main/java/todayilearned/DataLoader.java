package todayilearned;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.util.HomePageResults;
import todayilearned.util.HtmlService;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Configuration
public class DataLoader {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HtmlService htmlService;

    @Autowired
    HomePageResults homePageResults;

    @Bean
    public CommandLineRunner loadData(SubmissionRepository submissionRepo, UserRepository userRepo) throws IOException {
        return args -> {
            /* this configuration is necessary for using APIs such as Algolia */
            java.security.Security.setProperty("networkaddress.cache.ttl" , "60");

            User joe = new User("jstrauss24@bfhsla.org", "cuppajoe", passwordEncoder.encode("password"));
            User linus = new User("linus@kernel.org", "linus", passwordEncoder.encode("password"));
            final LocalDateTime dateTime = LocalDateTime.now();
            ObjectMapper mapper = new ObjectMapper();
            Submission submissionToSave;
            userRepo.save(joe);
            userRepo.save(linus);
            submissionRepo.deleteAll();

            BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/static/submission-titles.json"));
            String[] titles = mapper.readValue(reader.readLine(), String[].class);
            reader = Files.newBufferedReader(Paths.get("src/main/resources/static/submission-bodies.json"));
            String[] bodies = mapper.readValue(reader.readLine(), String[].class);
            for (int i = 0; i < 45; i += 2) {
                submissionToSave = new Submission(joe, dateTime, i + ": " + titles[i], bodies[i], htmlService.markdownToHtml(bodies[i]));
                submissionRepo.save(submissionToSave);
                submissionToSave = new Submission(linus, dateTime, (i+1) + ": " + titles[i+1], bodies[i+1], htmlService.markdownToHtml(bodies[i+1]));
                submissionRepo.save(submissionToSave);
            }
            homePageResults.refreshSubmissions();
        };
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
}
