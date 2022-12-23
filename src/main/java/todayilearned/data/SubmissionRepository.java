package todayilearned.data;

import org.springframework.data.jpa.repository.JpaRepository;
import todayilearned.Submission;
import todayilearned.User;

import java.util.ArrayList;
import java.util.Date;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    ArrayList<Submission> findByAuthor(User author);

    ArrayList<Submission> findByPostedOnAfter(Date date);
}
