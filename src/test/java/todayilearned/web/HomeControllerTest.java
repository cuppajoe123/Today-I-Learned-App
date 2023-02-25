package todayilearned.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import todayilearned.model.Submission;
import todayilearned.TodayILearnedApplication;
import todayilearned.model.User;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.security.SecurityConfig;
import todayilearned.util.HomePageResults;
import todayilearned.util.HtmlService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(HomeController.class)
@ContextConfiguration(classes = {SecurityConfig.class, TodayILearnedApplication.class})
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HtmlService htmlService;

    @MockBean
    private SubmissionRepository submissionRepo;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private HomePageResults homePageResults;

    private WebClient webClient;

    /* Domain objects used in each test */
    private final User user = new User(0L, "jstrauss24@bfhsla.org", "cuppajoe", "password", true);
    private final LocalDateTime dateTime = LocalDateTime.now();
    private final ArrayList<Submission> submissions = new ArrayList<>();

    @BeforeEach
    public void setup() {
        webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .contextPath("")
                .build();
        String body = "bodytext";
        String htmlBody = htmlService.markdownToHtml(body);
        for (long i = 0; i < 30; i++) {
            submissions.add(new Submission(i, user, dateTime, i + ". This will most likely be the average length of a title", body, htmlBody));
        }
    }

    @Test
    public void verifyFilledHomePageResults() throws Exception {
        when(homePageResults.getTopSubmissions()).thenReturn(submissions);

        HtmlPage homePage = webClient.getPage("http://localhost:8080/");
        List<String> results = homePage.getByXPath("//div[@class = 'submission']");
        assertEquals(20, results.size());
    }

    @Test
    public void verifyIncompleteHomePageResults() throws Exception {
        when(homePageResults.getTopSubmissions()).thenReturn(submissions);

        HtmlPage homePage = webClient.getPage("http://localhost:8080/?p=1");
        List<String> results = homePage.getByXPath("//div[@class = 'submission']");
        assertEquals(10, results.size());
    }
}
