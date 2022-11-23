package todayilearned.data;

import org.springframework.data.repository.CrudRepository;
import todayilearned.Submission;
import todayilearned.User;

import java.util.ArrayList;
import java.util.List;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {

    ArrayList<Submission> findByAuthor(User author);
}
