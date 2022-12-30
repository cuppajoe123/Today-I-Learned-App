package todayilearned.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
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
import todayilearned.security.UserRepositoryUserDetailsService;
import todayilearned.util.HtmlService;
import todayilearned.web.HomeController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


    WebClient webClient;

    @BeforeEach
    public void setup() {
        webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .contextPath("")
                .build();
    }

    @Test
    public void verifyHomePageResults() throws Exception {
        User joe = new User("jstrauss24@bfhsla.org", "cuppajoe", "password");
        final String body = "bodytext";
        final Date date = new Date();
        List<Submission> submissions = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            submissions.add(new Submission(joe, date, i + ". This will most likely be the average length of a title", body, htmlService.markdownToHtml(body)));
        }
        PageRequest pageRequest = PageRequest.of(0, 15);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), submissions.size());
        Page<Submission> page = new PageImpl<>(submissions.subList(start, end), pageRequest, submissions.size());
        when(submissionRepo.findAll(pageRequest)).thenReturn(page);
        when(htmlService.markdownToHtml(body)).thenReturn("<p>bodytext</p>");

        HtmlPage homePage = webClient.getPage("http://localhost:8080/");
        List<String> results = homePage.getByXPath("//div[@class = 'submission']");
        assertEquals(results.size(), 15);

        pageRequest = PageRequest.of(1, 15);
        start = (int) pageRequest.getOffset();
        end = Math.min((start + pageRequest.getPageSize()), submissions.size());
        page = new PageImpl<>(submissions.subList(start, end), pageRequest, submissions.size());
        when(submissionRepo.findAll(pageRequest)).thenReturn(page);
        homePage = webClient.getPage("http://localhost:8080/?p=1");
        results = homePage.getByXPath("//div[@class = 'submission']");
        assertEquals(results.size(), 15);

        pageRequest = PageRequest.of(2, 15);
        start = (int) pageRequest.getOffset();
        end = Math.min((start + pageRequest.getPageSize()), submissions.size());
        page = new PageImpl<>(submissions.subList(start, end), pageRequest, submissions.size());
        when(submissionRepo.findAll(pageRequest)).thenReturn(page);
        homePage = webClient.getPage("http://localhost:8080/?p=2");
        results = homePage.getByXPath("//div[@class = 'submission']");
        assertEquals(results.size(), 10);
    }
}
