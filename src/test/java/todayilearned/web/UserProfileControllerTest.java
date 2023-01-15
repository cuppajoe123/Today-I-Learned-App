package todayilearned.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import todayilearned.Submission;
import todayilearned.TodayILearnedApplication;
import todayilearned.User;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.security.SecurityConfig;
import todayilearned.util.HtmlService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    public void getUserFeed() throws Exception {
        User joe = new User("jstrauss24@bfhsla.org", "cuppajoe", "password");
        final String body = "bodytext";
        when(htmlService.markdownToHtml(body)).thenReturn("<p>bodytext</p>");
        ArrayList<Submission> submissions = new ArrayList<>(List.of(new Submission(joe, LocalDateTime.now(), "First post", body, htmlService.markdownToHtml(body)), new Submission(joe, LocalDateTime.now(), "Second post", body, htmlService.markdownToHtml(body))));
        when(submissionRepo.findByAuthor(joe)).thenReturn(submissions);
        when(userRepo.findByUsername("cuppajoe")).thenReturn(joe);

        this.mockMvc.perform(get("/user/" + joe.getUsername())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("First post")))
                .andExpect(content().string(containsString("Second post")))
                .andExpect(content().string(containsString("cuppajoe")));
    }
}
