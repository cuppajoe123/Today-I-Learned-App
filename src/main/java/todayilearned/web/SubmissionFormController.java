package todayilearned.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import todayilearned.Submission;
import todayilearned.User;
import todayilearned.data.SubmissionRepository;
import todayilearned.util.HtmlService;

import javax.validation.Valid;

@Controller
@RequestMapping("/submit")
public class SubmissionFormController {

    private SubmissionRepository submissionRepo;
    private HtmlService htmlService;

    public SubmissionFormController(SubmissionRepository submissionRepo, HtmlService htmlService) {
        this.submissionRepo = submissionRepo;
        this.htmlService = htmlService;
    }

    @GetMapping()
    public String submissionForm() {
        return "submissionForm";
    }

    @PostMapping()
    public String processSubmission(@Valid Submission submission, Errors errors, @AuthenticationPrincipal User author) {
        if (errors.hasErrors())
            return "submissionForm";
        submission.setAuthor(author);
        submission.setHtmlBody(htmlService.markdownToHtml(submission.getBody()));
        submissionRepo.save(submission);
        return "redirect:/";
    }


}
