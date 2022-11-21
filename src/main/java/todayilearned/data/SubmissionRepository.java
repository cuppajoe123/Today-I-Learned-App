package todayilearned.data;

import org.springframework.data.repository.CrudRepository;
import todayilearned.Submission;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {
}
