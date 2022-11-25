package todayilearned.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import todayilearned.Submission;
import todayilearned.User;
import todayilearned.data.SubmissionRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/submit")
public class SubmissionController {

    private SubmissionRepository submissionRepo;

    public SubmissionController(SubmissionRepository submissionRepo) {
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
