package todayilearned.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import todayilearned.Submission;
import todayilearned.data.SubmissionRepository;

@Controller
@RequestMapping("/")
@Slf4j
public class SubmissionController {

    private SubmissionRepository submissionRepo;

    public SubmissionController(SubmissionRepository submissionRepo) {
        this.submissionRepo = submissionRepo;
    }

    @GetMapping
    public void homePage() {
        Iterable<Submission> submissions = submissionRepo.findAll();
        for (Submission submission : submissions) {
            log.info("Submission: {}", submission);
            System.out.println(submission);
        }
    }


}
