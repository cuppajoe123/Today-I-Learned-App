package todayilearned.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import todayilearned.TodayILearnedApplication;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.security.SecurityConfig;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(SubmissionFormController.class)
@ContextConfiguration(classes = {SecurityConfig.class, TodayILearnedApplication.class})
public class SubmissionFormControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SubmissionRepository submissionRepo;

    @MockBean
    UserRepository userRepo;

    @Test
    @WithMockUser(roles = "USER")
    public void processSubmission() throws Exception {
        mockMvc.perform(post("/submit").with(csrf())
            .content("title=Interesting+Tite&body=Interesting+Description")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().stringValues("Location", "/"));
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

}
