package todayilearned.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import todayilearned.Submission;
import todayilearned.User;
import todayilearned.data.SubmissionRepository;

@Controller
@RequestMapping("/submit")
public class SubmissionFormController {

    private SubmissionRepository submissionRepo;

    public SubmissionFormController(SubmissionRepository submissionRepo) {
        this.submissionRepo = submissionRepo;
    }

    @GetMapping()
    public String submissionForm() {
        return "submissionForm";
    }

    @PostMapping()
    public String processSubmission(Submission submission, @AuthenticationPrincipal User author) {
        submission.setAuthor(author);
        submissionRepo.save(submission);
        return "redirect:/";
    }


}
