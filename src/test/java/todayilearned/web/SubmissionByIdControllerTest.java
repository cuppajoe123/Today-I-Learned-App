package todayilearned.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import todayilearned.SpringSecurityUserTestConfig;
import todayilearned.model.Submission;
import todayilearned.TodayILearnedApplication;
import todayilearned.model.User;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.security.SecurityConfig;
import todayilearned.util.HtmlService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubmissionByIdController.class)
@ContextConfiguration(classes = {SecurityConfig.class, TodayILearnedApplication.class, SpringSecurityUserTestConfig.class})
public class SubmissionByIdControllerTest {

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

    @Test
    public void getSubmissionById() throws Exception {
        final String body = "bodytext";
        when(htmlService.markdownToHtml(body)).thenReturn("<p>bodytext</p>");
        Optional<Submission> submission = Optional.of(new Submission(1L, user, LocalDateTime.now(), "Interesting title", body, htmlService.markdownToHtml(body)));

        when(submissionRepo.findById(submission.get().getId())).thenReturn(submission);
        when(userRepo.findByUsername("cuppajoe")).thenReturn(Optional.of(user));
        this.mockMvc.perform(get("/submission/" + submission.get().getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Interesting title")))
                .andExpect(content().string(containsString("bodytext")));
    }

    @Test
    @WithUserDetails("cuppajoe")
    public void edit() throws Exception {
        final String body = "bodytext";
        when(htmlService.markdownToHtml(body)).thenReturn("<p>bodytext</p>");
        Optional<Submission> submission = Optional.of(new Submission(1L, user, LocalDateTime.now(), "Interesting title", body, htmlService.markdownToHtml(body)));

        when(submissionRepo.findById(submission.get().getId())).thenReturn(submission);
        when(userRepo.findByUsername("cuppajoe")).thenReturn(Optional.of(user));
        this.mockMvc.perform(get("/submission/" + submission.get().getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Interesting title")))
                .andExpect(content().string(containsString("bodytext")))
                .andExpect(content().string(containsString("edit")));
    }


}