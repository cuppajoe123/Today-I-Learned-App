package todayilearned.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import todayilearned.Submission;
import todayilearned.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SubmissionRepositoryTest {

    @Autowired
    SubmissionRepository submissionRepo;

    @Autowired
    UserRepository userRepo;

    User joe;
    Date today;
    ArrayList<Submission> submissions;

    @BeforeEach
    private void beforeAll() {
        joe = new User("jstrauss24@bfhsla.org", "cuppajoe", "password");
        userRepo.save(joe);
        today = new Date();
        submissions = new ArrayList<>();
        submissions.add(new Submission(joe, today, "This is my first submission", "Body text"));
        submissions.add(new Submission(joe, today, "This is my second submission", "Body text"));
    }
    @Test
    public void saveAndFetchSubmission() {
        Submission savedSubmission = submissionRepo.save(submissions.get(0));
        assertThat(savedSubmission.getId()).isNotNull();

        Submission fetchedSubmission = submissionRepo.findById(savedSubmission.getId()).get();
        assertThat(fetchedSubmission.getPostedOn().equals(today));
        assertThat(fetchedSubmission.getAuthor().equals(joe));
        assertThat(fetchedSubmission.getTitle().equals("This is my first submission"));
        assertThat(fetchedSubmission.getBody().equals("Body text"));
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
