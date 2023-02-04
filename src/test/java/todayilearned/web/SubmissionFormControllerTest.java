package todayilearned.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubmissionFormController.class)
@ContextConfiguration(classes = {SecurityConfig.class, TodayILearnedApplication.class, SpringSecurityUserTestConfig.class})
public class SubmissionFormControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private User user;

    @MockBean
    SubmissionRepository submissionRepo;

    @MockBean
    UserRepository userRepo;

    @MockBean
    HtmlService htmlService;

    @Test
    @WithUserDetails("cuppajoe")
    public void processSubmission() throws Exception {
        mockMvc.perform(post("/submit").with(csrf())
            .content("title=Interesting+Tite&body=Interesting+Description")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().stringValues("Location", "/"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void processInvalidSubmission() throws Exception {
        mockMvc.perform(post("/submit").with(csrf())
                .content("")) // empty title and body
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("A title is required")))
                .andExpect(content().string(containsString("A body is required")));
    }

    @Test
    public void attemptGetSubmissionFormWhenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/submit"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().stringValues("Location", "http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void attemptGetSubmissionFormWhenLoggedIn() throws Exception {
        mockMvc.perform(get("/submit"))
            .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("cuppajoe")
    public void authorizedGetEditForm() throws Exception {
        final long id = 0L;
        final String title = "Interesting Title";
        final String body = "bodytext";
        Optional<Submission> submission = Optional.of(new Submission(id, user, LocalDateTime.now(), title, body, htmlService.markdownToHtml(body)));
        when(submissionRepo.findById(id)).thenReturn(submission);
        mockMvc.perform(get("/submit/?edit=0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(title)))
                .andExpect(content().string(containsString(body)));
    }

    @Test
    public void unauthorizedAttemptGetEditForm() throws Exception {
        final long id = 0L;
        final String title = "Interesting Title";
        final String body = "bodytext";
        Optional<Submission> submission = Optional.of(new Submission(id, user, LocalDateTime.now(), title, body, htmlService.markdownToHtml(body)));
        when(submissionRepo.findById(id)).thenReturn(submission);
        mockMvc.perform(get("/submit/?edit=0"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails("cuppajoe")
    public void authorizedSubmitEditForm() throws Exception {
        final long id = 0L;
        final String title = "Interesting Title";
        final String body = "bodytext";
        Optional<Submission> submission = Optional.of(new Submission(id, user, LocalDateTime.now(), title, body, htmlService.markdownToHtml(body)));
        when(submissionRepo.findById(id)).thenReturn(submission);
        mockMvc.perform(post("/submit/?edit=0").with(csrf())
                .content("title=Interesting+Title&body=Interesting+Description")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues("Location", "/"));
    }

    @Test
    public void unauthorizedAttemptSubmitEditForm() throws Exception {
        final long id = 0L;
        final String title = "Interesting Title";
        final String body = "bodytext";
        Optional<Submission> submission = Optional.of(new Submission(id, user, LocalDateTime.now(), title, body, htmlService.markdownToHtml(body)));
        when(submissionRepo.findById(id)).thenReturn(submission);
        mockMvc.perform(post("/submit/?edit=0").with(csrf())
                .content("title=Interesting+Title&body=Interesting+Description")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is4xxClientError());
    }
}