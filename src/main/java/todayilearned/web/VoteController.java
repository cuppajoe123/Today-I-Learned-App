package todayilearned.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import todayilearned.Submission;
import todayilearned.User;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;

import java.util.Optional;

@Controller
@RequestMapping("/vote")
@Slf4j
public class VoteController {

    private SubmissionRepository submissionRepo;
    private UserRepository userRepo;

    public VoteController(SubmissionRepository submissionRepo, UserRepository userRepo) {
        this.submissionRepo = submissionRepo;
        this.userRepo = userRepo;
    }
    /* Implement a POST mapping that will read the JSON sent from the front end,
    perform the necessary checks and database updates, and finally send back a response
    The JSON will look like:
        {
            "submissionId": <id>
        }
     */
    @PostMapping()
    @ResponseBody
    public ResponseEntity<Void> processVote(@RequestParam(name = "submissionId") Long submissionId, @AuthenticationPrincipal User user) {
        Optional<Submission> submissionOptional = submissionRepo.findById(submissionId);
        if (submissionOptional.isPresent() && !user.getVotedSubmissions().contains(submissionId)) {
            Submission submission = submissionOptional.get();
            submission.incrementPoints();
            submissionRepo.save(submission);
            user.addSubmission(submission.getId());
            log.info("Submission " + submission.getId() + " has " + submission.getPoints() + " points");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}