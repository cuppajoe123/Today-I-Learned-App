package todayilearned;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.security.SecurityConfig;
import todayilearned.web.HomeController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(HomeController.class)
class TodayILearnedApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SubmissionRepository submissionRepo;

	@MockBean
	private UserRepository userRepo;

	@Test
	@WithMockUser(roles = "USER")
	public void getHomePage() throws Exception {
		User joe = new User("jstrauss24@bfhsla.org", "cuppajoe", "password");
		Iterable<Submission> submissions = List.of(new Submission(joe, new Date(), "First post", "bodytext"), new Submission(joe, new Date(), "Second post", "bodytext"));
		when(submissionRepo.findAll()).thenReturn(submissions);

		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
			.andExpect(content().string(containsString("First post")))
			.andExpect(content().string(containsString("Second post")))
			.andExpect(content().string(containsString("cuppajoe")));
	}

}
