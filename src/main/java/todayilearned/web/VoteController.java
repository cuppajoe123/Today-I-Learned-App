package todayilearned.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import todayilearned.Submission;
import todayilearned.User;
import todayilearned.VoteRequest;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;

import java.util.Optional;

@Controller
@RequestMapping("/vote")
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
    public String processVote(@RequestBody VoteRequest voteRequest, @AuthenticationPrincipal User user) {
        System.out.println(voteRequest);
        Optional<Submission> submissionOptional = submissionRepo.findById(voteRequest.getSubmissionId());
        if (submissionOptional.isPresent() && !user.getVotedSubmissions().contains(voteRequest.getSubmissionId())) {
            Submission submission = submissionOptional.get();
            submission.incrementPoints();
            submissionRepo.save(submission);
            user.addSubmission(submission.getId());
            return "{\"Votes\": " + submission.getPoints() + "}";
        }
        return "{\"Status\": \"Forbidden\"";
    }

}