package todayilearned.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import todayilearned.model.Submission;
import todayilearned.model.User;
import todayilearned.data.SubmissionRepository;
import todayilearned.util.HomePageResults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@Slf4j
public class HomeController {
    SubmissionRepository submissionRepo;

    HomePageResults homePageResults;

    @Autowired
    Environment env;

    final int pageSize = 20;

    public HomeController(SubmissionRepository submissionRepo, HomePageResults homePageResults) {
        this.submissionRepo = submissionRepo;
        this.homePageResults = homePageResults;
    }

    @GetMapping
    public String homePage(@RequestParam(defaultValue = "0", name = "p") int pageNumber, Model model, @AuthenticationPrincipal User user) {
        /* Check if database is empty */
        if (homePageResults.getTopSubmissions().size() == 0)
            return "home";

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<Submission> submissionSubList, topSubmissions = homePageResults.getTopSubmissions();
        if (pageNumber == 0)
            try {
                submissionSubList = topSubmissions.subList(0, pageRequest.getPageSize());
            } catch (IndexOutOfBoundsException e) {
                submissionSubList = topSubmissions.subList(0, topSubmissions.size());
            }
        else {
            try {
                submissionSubList = topSubmissions.subList((pageRequest.getPageNumber()) * pageRequest.getPageSize(), ((pageRequest.getPageNumber() + 1) * pageRequest.getPageSize()));
            } catch (IndexOutOfBoundsException e) {
                submissionSubList = topSubmissions.subList((pageRequest.getPageNumber()) * pageRequest.getPageSize(), topSubmissions.size());
            }
        }
        Page<Submission> page = new PageImpl<>(submissionSubList, pageRequest, topSubmissions.size());
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
        model.addAttribute("profile", env.getActiveProfiles());
        return "home";
    }

    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("profile", env.getActiveProfiles());
        return "search";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
