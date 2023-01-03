package todayilearned.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public String submissionForm(@RequestParam(required = false, name = "edit")  Long submissionId, @AuthenticationPrincipal User user, Model model) {
        if (submissionId != null) {
            Optional<Submission> submission = submissionRepo.findById(submissionId);
            if (submission.isPresent()) {
                if (submission.get().getAuthor().equals(user)) {
                    model.addAttribute("submission", submission.get());
                    return "submissionForm";
                } else
                    return "redirect:/";
            }
        }
        model.addAttribute("submission", new Submission());
        return "submissionForm";
    }

    @PostMapping()
    public String processSubmission(@RequestParam(required = false, name = "edit")  Long submissionId, @ModelAttribute @Valid Submission submission, Errors errors, @AuthenticationPrincipal User author) throws ForbiddenRequestException {
        if (errors.hasErrors())
            return "submissionForm";
        if (submissionId != null) {
            Optional<Submission> submissionToEdit = submissionRepo.findById(submissionId);
            if (submissionToEdit.isPresent() && submissionToEdit.get().getAuthor().equals(author))
                submission.setId(submissionId);
            else
                throw new ForbiddenRequestException("Submission not found or not authorized");
        }
        submission.setAuthor(author);
        submission.setHtmlBody(htmlService.markdownToHtml(submission.getBody()));
        submissionRepo.save(submission);
        return "redirect:/";
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    private static class ForbiddenRequestException extends Exception {
        public ForbiddenRequestException(String msg) {
            super(msg);
        }
    }
}
