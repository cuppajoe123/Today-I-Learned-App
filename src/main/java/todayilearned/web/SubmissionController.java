package todayilearned.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import todayilearned.Submission;
import todayilearned.data.SubmissionRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class SubmissionController {

    private SubmissionRepository submissionRepo;

    public SubmissionController(SubmissionRepository submissionRepo) {
        this.submissionRepo = submissionRepo;
    }

    @GetMapping
    public String homePage() {
        Iterable<Submission> submissions = submissionRepo.findAll();
        for (Submission submission : submissions) {
            log.info("Submission: {}", submission);
            System.out.println(submission);
        }
        return "home";
    }

    @GetMapping("/submit")
    public String submissionForm() {
        return "submissionForm";
    }

    @PostMapping("/submit")
    public String processSubmission(Submission submission) {
        submissionRepo.save(submission);
        return "redirect:/";
    }

    @ModelAttribute(name = "submissions")
    public List<Submission> addSubmissionsToModel(Model model) {
        List<Submission> submissions = new ArrayList<>();
        submissionRepo.findAll().forEach(i -> submissions.add(i));
        return submissions;

    }


}
