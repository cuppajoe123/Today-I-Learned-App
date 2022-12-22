package todayilearned.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import todayilearned.Submission;
import todayilearned.User;
import todayilearned.data.SubmissionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@Slf4j
public class HomeController {

    SubmissionRepository submissionRepo;

    final int pageSize = 15;

    public HomeController(SubmissionRepository submissionRepo) {
        this.submissionRepo = submissionRepo;
    }

    @GetMapping
    public String homePage(@RequestParam(defaultValue = "0", name = "p") int pageNumber, Model model, @AuthenticationPrincipal User user) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Submission> page = submissionRepo.findAll(pageRequest);
        log.info("User: " + user);
        /* This Map maps each post in a page to a boolean indicating whether the current user has already voted on it. */
        Map<Submission, Boolean> upvotedSubmissionsMap = new HashMap<>();
        if (user != null) {
            for (Submission submission : page) {
                upvotedSubmissionsMap.put(submission, user.getVotedSubmissions().contains(submission.getId()));
            }
        } else {
            for (Submission submission : page) {
                upvotedSubmissionsMap.put(submission, false);
            }
        }

        model.addAttribute("submissions", page);
        model.addAttribute("upvotedSubmissionsMap", upvotedSubmissionsMap);
        return "home";
    }


    public void addSubmissionsToModel(@RequestParam(defaultValue = "0", name = "p") int pageNumber, Model model, @AuthenticationPrincipal User user) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Submission> page = submissionRepo.findAll(pageRequest);
        model.addAttribute("submissions", page);
        log.info("User: " + user);
        Map<Submission, Boolean> upvotedSubmissionsMap = new HashMap<>();
    }
}
