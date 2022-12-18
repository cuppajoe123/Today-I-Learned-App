package todayilearned.data;

import org.springframework.data.jpa.repository.JpaRepository;
import todayilearned.Submission;
import todayilearned.User;

import java.util.ArrayList;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    ArrayList<Submission> findByAuthor(User author);
}
