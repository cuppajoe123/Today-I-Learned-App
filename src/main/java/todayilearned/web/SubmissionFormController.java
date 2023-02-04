package todayilearned.web;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.XmlReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.XMLReader;
import todayilearned.Submission;
import todayilearned.User;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.util.HtmlService;

import javax.validation.Valid;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/submit")
public class SubmissionFormController {

    private SubmissionRepository submissionRepo;
    private UserRepository userRepo;
    private HtmlService htmlService;

    public SubmissionFormController(SubmissionRepository submissionRepo, UserRepository userRepo, HtmlService htmlService) {
        this.submissionRepo = submissionRepo;
        this.userRepo = userRepo;
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
    public String processSubmission(@RequestParam(required = false, name = "edit")  Long submissionId, @ModelAttribute @Valid Submission submission, Errors errors, @AuthenticationPrincipal User author) throws ForbiddenRequestException, IOException, FeedException {
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
        if (submissionId == null) { // if creating a new submission
            updateRssFeed(submission, author);
        }
        return "redirect:/";
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    private static class ForbiddenRequestException extends Exception {
        public ForbiddenRequestException(String msg) {
            super(msg);
        }
    }

    private void updateRssFeed(Submission submission, User author) throws IOException, FeedException {
        /* Create an RSS entry */
        SyndEntry newEntry = new SyndEntryImpl();
        newEntry.setTitle(submission.getTitle());
        newEntry.setLink("http://localhost:8080/" + submission.getId());

        /* Get RSS feed from user, update entries, write it back to DB */
        SyndFeedImpl feedToUpdate = author.getRssFeed();
        List<SyndEntry> entries = feedToUpdate.getEntries();
        entries.add(newEntry);
        feedToUpdate.setEntries(entries);
        author.setRssFeed(feedToUpdate);
        userRepo.save(author);
    }
}
