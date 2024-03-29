package todayilearned;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.SerializationUtils;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.model.Submission;
import todayilearned.model.User;
import todayilearned.util.HomePageResults;
import todayilearned.util.HtmlService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoader {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HtmlService htmlService;

    @Autowired
    HomePageResults homePageResults;

    @Bean
    @Profile("dev")
    public CommandLineRunner loadData(SubmissionRepository submissionRepo, UserRepository userRepo) throws IOException {
        return args -> {
            /* this configuration is necessary for using APIs such as Algolia */
            java.security.Security.setProperty("networkaddress.cache.ttl" , "60");

            User joe = new User("jstrauss24@bfhsla.org", "cuppajoe", passwordEncoder.encode("password"));
            User linus = new User("linus@kernel.org", "linus", passwordEncoder.encode("password"));
            joe.setEnabled(true);
            linus.setEnabled(true);
            final LocalDateTime dateTime = LocalDateTime.now();
            ObjectMapper mapper = new ObjectMapper();
            Submission submissionToSave;
            userRepo.save(joe);
            userRepo.save(linus);
            submissionRepo.deleteAll();

            /* RSS setup: only for user joe */
            List<SyndEntry> joeFeed = new ArrayList<>();
            SyndEntry entryToAdd;

            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/static/submission-titles.json")));
            String[] titles = mapper.readValue(reader.readLine(), String[].class);
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/static/submission-bodies.json")));
            String[] bodies = mapper.readValue(reader.readLine(), String[].class);
            for (int i = 0; i < 45; i += 2) {
                submissionToSave = new Submission(joe, dateTime, i + ": " + titles[i], bodies[i], htmlService.markdownToHtml(bodies[i]));
                submissionRepo.save(submissionToSave);
                entryToAdd = new SyndEntryImpl();
                entryToAdd.setTitle(submissionToSave.getTitle());
                entryToAdd.setLink("http://localhost:8080/" + submissionToSave.getId());
                joeFeed.add(entryToAdd);
                submissionToSave = new Submission(linus, dateTime, (i+1) + ": " + titles[i+1], bodies[i+1], htmlService.markdownToHtml(bodies[i+1]));
                submissionRepo.save(submissionToSave);
            }

            SyndFeedImpl feed = (SyndFeedImpl) SerializationUtils.deserialize(joe.getRssFeed());
            feed.setEntries(joeFeed);
            joe.setRssFeed(SerializationUtils.serialize(feed));
            userRepo.save(joe);

            homePageResults.refreshSubmissions();
        };
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Profile("dev")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
}
