package todayilearned.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import todayilearned.Submission;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/submission/{submissionId}")
public class SubmissionByIdController {

    private SubmissionRepository submissionRepo;
    private UserRepository userRepo;

    public SubmissionByIdController(SubmissionRepository submissionRepo, UserRepository userRepo) {
        this.submissionRepo = submissionRepo;
        this.userRepo = userRepo;
    }

    @GetMapping()
    public String userSubmissionById() {
        return "submissionById";
    }

    @ModelAttribute(name = "submission")
    public Submission findSubmissionById(@PathVariable("submissionId") Long id, Model model) {
        Optional<Submission> submission = submissionRepo.findById(id);
        if (submission.isPresent())
            return submission.get();
        return new Submission(null, new Date(), "Does not exist", "Does not exist");
    }
}
