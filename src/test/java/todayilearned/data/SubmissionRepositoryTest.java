package todayilearned.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import todayilearned.Submission;
import todayilearned.User;
import todayilearned.util.HtmlService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
public class SubmissionRepositoryTest {

    @Autowired
    SubmissionRepository submissionRepo;

    @Autowired
    UserRepository userRepo;

    @MockBean
    HtmlService htmlService;

    User joe;
    LocalDateTime today;
    ArrayList<Submission> submissions;

    @BeforeEach
    private void beforeAll() {
        joe = new User("jstrauss24@bfhsla.org", "cuppajoe", "password");
        userRepo.save(joe);
        today = LocalDateTime.now();
        String body = "bodytext";
        when(htmlService.markdownToHtml(body)).thenReturn("<p>bodytext</p>");
        submissions = new ArrayList<>();
        submissions.add(new Submission(joe, today, "This is my first submission", body, htmlService.markdownToHtml(body)));
        submissions.add(new Submission(joe, today, "This is my second submission", body, htmlService.markdownToHtml(body)));
    }
    @Test
    public void saveAndFetchSubmission() {
        Submission savedSubmission = submissionRepo.save(submissions.get(0));
        assertThat(savedSubmission.getId()).isNotNull();

        Submission fetchedSubmission = submissionRepo.findById(savedSubmission.getId()).get();
        assertThat(fetchedSubmission.getPostedOn().equals(today));
        assertThat(fetchedSubmission.getAuthor().equals(joe));
        assertThat(fetchedSubmission.getTitle().equals("This is my first submission"));
        assertThat(fetchedSubmission.getBody().equals("bodytext"));
    }

    @Test
    public void saveAndFetchSubmissionsByAuthor() {
        submissionRepo.save(submissions.get(0));
        submissionRepo.save(submissions.get(1));

        ArrayList<Submission> fetchedSubmissions = submissionRepo.findByAuthor(joe);
        assertThat(fetchedSubmissions.get(0).equals(submissions.get(0)));
        assertThat(fetchedSubmissions.get(1).equals(submissions.get(1)));
        assertThat(fetchedSubmissions.size() == 2);
    }

}
