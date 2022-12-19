package todayilearned.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import todayilearned.User;
import todayilearned.VoteRequest;

@Controller
@RequestMapping("/vote")
public class VoteController {
    /* Implement a POST mapping that will read the JSON sent from the front end,
    perform the necessary checks and database updates, and finally send back a response
    The JSON will look like:
        {
            "jsessionid": "<sessionId>",
            "submissionId": <id>
        }
     */
    @PostMapping()
    public ResponseEntity processVote(@RequestBody VoteRequest voteRequest) {
        System.out.println(voteRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}