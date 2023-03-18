package todayilearned.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.util.SerializationUtils;
import todayilearned.model.Submission;
import todayilearned.TodayILearnedApplication;
import todayilearned.model.User;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.security.SecurityConfig;
import todayilearned.util.HtmlService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(UserProfileController.class)
@ContextConfiguration(classes = {SecurityConfig.class, TodayILearnedApplication.class})
public class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HtmlService htmlService;

    @MockBean
    private SubmissionRepository submissionRepo;

    @MockBean
    private UserRepository userRepo;

    private WebClient webClient;

    /* domain objects used in each test */
    private final User user = new User("jstrauss24@bfhsla.org", "cuppajoe", "password");
    private final LocalDateTime dateTime = LocalDateTime.now();
    private final ArrayList<Submission> submissions = new ArrayList<>();
    private final long numSubmissions = 30L;

    @BeforeEach
    public void setup() {
        webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .contextPath("")
                .build();

        List<SyndEntry> entries = new ArrayList<>();

        String body = "bodytext";
        String htmlBody = htmlService.markdownToHtml(body);
        for (long i = 0; i < numSubmissions; i++) {
            submissions.add(new Submission(i, user, dateTime, i + ". foo bar", body, htmlBody));

            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(i + ". foo bar");
            entry.setLink("http://localhost:8080/user/" + user.getUsername());
            entries.add(entry);
        }
        SyndFeedImpl feed = (SyndFeedImpl) SerializationUtils.deserialize(user.getRssFeed());
        feed.setEntries(entries);
        user.setRssFeed(SerializationUtils.serialize(feed));
    }


    @Test
    public void getUserFeed() throws Exception {
        when(submissionRepo.findByAuthor(user)).thenReturn(submissions);
        when(userRepo.findByUsername("cuppajoe")).thenReturn(Optional.of(user));

        HtmlPage userFeed = webClient.getPage("http://localhost:8080/user/" + user.getUsername());
        List<String> results = userFeed.getByXPath("//div[@class = 'submission']");
        assertEquals(numSubmissions, results.size());
    }

    @Test
    public void getUserRssFeed() throws Exception {
        when(submissionRepo.findByAuthor(user)).thenReturn(submissions);
        when(userRepo.findByUsername("cuppajoe")).thenReturn(Optional.of(user));

        XmlPage rssFeed = webClient.getPage("http://localhost:8080/user/" + user.getUsername() + "/rss");
        List<String> results = rssFeed.getByXPath("//item");
        assertEquals(numSubmissions, results.size());
    }
}
