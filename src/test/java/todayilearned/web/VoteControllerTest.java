package todayilearned.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import todayilearned.SpringSecurityUserTestConfig;
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
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
@ContextConfiguration(classes = {SecurityConfig.class, TodayILearnedApplication.class, SpringSecurityUserTestConfig.class})
public class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private User user;

    @MockBean
    private HtmlService htmlService;

    @MockBean
    private SubmissionRepository submissionRepo;

    @MockBean
    private UserRepository userRepo;

    /* Domain objects used in each test */
    private final LocalDateTime dateTime = LocalDateTime.now();
    ArrayList<Submission> submissions = new ArrayList<>();

    @BeforeEach
    public void setup() {
        for (long i = 0; i < 25; i++) {
            String body = "bodytext";
            submissions.add(new Submission(i, user, dateTime, i + ". This will most likely be the average length of a title", body, htmlService.markdownToHtml(body)));
        }
    }

    @Test
    @WithUserDetails("cuppajoe")
    public void authenticatedUpvoteTest() throws Exception {
        Optional<Submission> submission = Optional.of(submissions.get(0));
        when(submissionRepo.findById(submission.get().getId())).thenReturn(submission);
        when(userRepo.save(user)).thenReturn(user);

        mockMvc.perform(post("/vote?submissionId=0").with(csrf()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/vote?submissionId=0").with(csrf()))
                .andExpect(status().is(403));
    }

    @Test
    public void loggedOutUpvoteTest() throws Exception {
        mockMvc.perform(post("/vote?submissionId=0").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }
}
