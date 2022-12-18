package todayilearned.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import todayilearned.Submission;
import todayilearned.data.SubmissionRepository;

import java.util.ArrayList;
import java.util.List;

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
    public String homePage() {
        return "home";
    }

    @ModelAttribute(name = "submissions")
    public Page<Submission> addSubmissionsToModel(@RequestParam(defaultValue = "0", name = "p") int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return submissionRepo.findAll(pageRequest);

    }
}
