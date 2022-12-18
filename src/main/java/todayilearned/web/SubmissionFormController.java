package todayilearned.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import todayilearned.Submission;
import todayilearned.User;
import todayilearned.data.SubmissionRepository;
import todayilearned.util.HtmlService;

import javax.validation.Valid;
import java.util.Optional;

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
    public String submissionForm(@RequestParam(required = false, name = "edit")  Long submissionId, Model model) {
        if (submissionId != null) {
            Optional<Submission> submission = submissionRepo.findById(submissionId);
            if (submission.isPresent()) {
                model.addAttribute("submission", submission.get());
                return "submissionForm";
            }
        }
        model.addAttribute("submission", new Submission());
        return "submissionForm";
    }

    // TODO: if an edit parameter is posted and is a valid id, update post instead of making a new one.
    @PostMapping()
    public String processSubmission(@ModelAttribute @Valid Submission submission, Errors errors, @AuthenticationPrincipal User author) {
        if (errors.hasErrors())
            return "submissionForm";
        submission.setAuthor(author);
        submission.setHtmlBody(htmlService.markdownToHtml(submission.getBody()));
        submissionRepo.save(submission);
        return "redirect:/";
    }


}
