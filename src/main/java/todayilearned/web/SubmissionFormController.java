package todayilearned.web;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import org.jsoup.parser.Parser;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.SerializationUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.model.AlgoliaSubmission;
import todayilearned.model.Submission;
import todayilearned.model.User;
import todayilearned.util.HtmlService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/submit")
public class SubmissionFormController {

    private SubmissionRepository submissionRepo;
    private UserRepository userRepo;
    private HtmlService htmlService;

    @Autowired
    private Environment env;

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
    public String processSubmission(@RequestParam(required = false, name = "edit")  Long submissionId, @ModelAttribute @Valid Submission submission, Errors errors, @AuthenticationPrincipal User author) throws ForbiddenRequestException {
        if (errors.hasErrors())
            return "submissionForm";

        /* Algolia setup */
        SearchClient client = DefaultSearchClient.create(env.getProperty("spring.custom.algolia-application-id"), env.getProperty("spring.custom.algolia-indexing-api-key"));
        SearchIndex<AlgoliaSubmission> index = client.initIndex(env.getProperty("spring.custom.algolia-index-name"), AlgoliaSubmission.class);

        if (submissionId != null) {
            Optional<Submission> submissionToEdit = submissionRepo.findById(submissionId);
            if (submissionToEdit.isPresent() && submissionToEdit.get().getAuthor().equals(author)) {
                submission.setId(submissionId);
                submission.setAuthor(submissionToEdit.get().getAuthor());
                /* Update Algolia index to reflect edits */
                index.saveObject(submission.convertToAlgoliaSubmission());
            }
            else
                throw new ForbiddenRequestException("Submission not found or not authorized");
        }
        submission.setAuthor(author);
        /* HTML sanitization */
//        Document doc = Jsoup.parse(submission.getBody());
//        Elements codeBlocks = doc.select("pre");
//        for (Element tag : codeBlocks) {
//            if (tag.html().matches("(.*)<[a-z](.*)")) {
//                String innerHTML = tag.html().replaceAll("<(?=[a-z])", "&lt;");
//                tag.html(innerHTML);
//            }
//        }

        /* Factor out into separate function */
        /* Try removing <pre> tags */


        PolicyFactory policy = new HtmlPolicyBuilder()
                .allowCommonBlockElements()
                .allowCommonInlineFormattingElements()
                .allowStyling()
                .toFactory();
        String safeInput = policy.sanitize(submission.getBody());
        safeInput = Parser.unescapeEntities(safeInput, true);
        submission.setHtmlBody(htmlService.markdownToHtml(safeInput));
        submission.setBody(safeInput);
        submission = submissionRepo.save(submission);
        if (submissionId == null) { // if creating a new submission
            updateRssFeed(submission, author);

            /* Save to Algolia */
            index.saveObject(submission.convertToAlgoliaSubmission());
        }
        return "redirect:/";
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    private static class ForbiddenRequestException extends Exception {
        public ForbiddenRequestException(String msg) {
            super(msg);
        }
    }

    private void updateRssFeed(Submission submission, User author) {
        /* Create an RSS entry */
        SyndEntry newEntry = new SyndEntryImpl();
        newEntry.setTitle(submission.getTitle());
        newEntry.setLink("http://" + env.getProperty("spring.custom.domain-name") + "/" + submission.getId());

        /* Get RSS feed from user, update entries, write it back to DB */
        SyndFeedImpl feedToUpdate = (SyndFeedImpl) SerializationUtils.deserialize(author.getRssFeed());
        List<SyndEntry> entries = feedToUpdate.getEntries();
        entries.add(newEntry);
        feedToUpdate.setEntries(entries);
        author.setRssFeed(SerializationUtils.serialize(feedToUpdate));
        userRepo.save(author);
    }
}
