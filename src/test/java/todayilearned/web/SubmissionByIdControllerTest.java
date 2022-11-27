package todayilearned.web;

import lombok.extern.slf4j.Slf4j;
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

import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubmissionByIdController.class)
@ContextConfiguration(classes = {SecurityConfig.class, TodayILearnedApplication.class})
public class SubmissionByIdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubmissionRepository submissionRepo;

    @MockBean
    private UserRepository userRepo;

    @Test
    public void getSubmissionById() throws Exception {
        User joe = new User(1L, "jstrauss24@bfhsla.org", "cuppajoe", "password");
        Optional<Submission> submission = Optional.of(new Submission(1L, joe, new Date(), "Interesting title", "Body text"));

        when(submissionRepo.findById(submission.get().getId())).thenReturn(submission);
        when(userRepo.findByUsername("cuppajoe")).thenReturn(joe);
        this.mockMvc.perform(get("/submission/" + submission.get().getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Interesting title")))
                .andExpect(content().string(containsString("Body text")));
    }
}
