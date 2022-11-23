package todayilearned.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import todayilearned.Submission;
import todayilearned.data.SubmissionRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class HomeController {

    SubmissionRepository submissionRepo;

    public HomeController(SubmissionRepository submissionRepo) {
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

    @ModelAttribute(name = "submissions")
    public List<Submission> addSubmissionsToModel(Model model) {
        List<Submission> submissions = new ArrayList<>();
        submissionRepo.findAll().forEach(i -> submissions.add(i));
        return submissions;

    }

}
