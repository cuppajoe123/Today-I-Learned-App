package todayilearned.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;
import todayilearned.Submission;
import todayilearned.TodayILearnedApplication;
import todayilearned.User;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.security.SecurityConfig;
import todayilearned.util.HtmlService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(HomeController.class)
@ContextConfiguration(classes = {SecurityConfig.class, TodayILearnedApplication.class})
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private HtmlService htmlService;

    @MockBean
    private SubmissionRepository submissionRepo;

    @MockBean
    private UserRepository userRepo;

    private WebClient webClient;

    /* Domain objects used in each test */
    private final User user = new User(0L, "jstrauss24@bfhsla.org", "cuppajoe", "password");
    private final Date date = new Date();
    ArrayList<Submission> submissions = new ArrayList<>();


    @BeforeEach
    public void setup() {
        webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .contextPath("")
                .build();
        for (long i = 0; i < 25; i++) {
            String body = "bodytext";
            submissions.add(new Submission(i, user, date, i + ". This will most likely be the average length of a title", body, htmlService.markdownToHtml(body)));
        }
    }

    @Test
    public void verifyFilledHomePageResults() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 15);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), submissions.size());
        Page<Submission> page = new PageImpl<>(submissions.subList(start, end), pageRequest, submissions.size());
        when(submissionRepo.findAll(pageRequest)).thenReturn(page);

        HtmlPage homePage = webClient.getPage("http://localhost:8080/");
        List<String> results = homePage.getByXPath("//div[@class = 'submission']");
        assertEquals(results.size(), 15);
    }

    @Test
    public void verifyIncompleteHomePageResults() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 15);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), submissions.size());
        Page<Submission> page = new PageImpl<>(submissions.subList(start, end), pageRequest, submissions.size());
        when(submissionRepo.findAll(pageRequest)).thenReturn(page);
        HtmlPage homePage = webClient.getPage("http://localhost:8080/?p=1");
        List<String> results = homePage.getByXPath("//div[@class = 'submission']");
        assertEquals(results.size(), 10);
    }
}
