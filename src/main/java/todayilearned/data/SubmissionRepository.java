package todayilearned.data;

import org.springframework.data.jpa.repository.JpaRepository;
import todayilearned.model.Submission;
import todayilearned.model.User;

import java.time.LocalDate;
import java.util.ArrayList;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    ArrayList<Submission> findByAuthor(User author);

    ArrayList<Submission> findByPostedOnAfter(LocalDate date);
}
