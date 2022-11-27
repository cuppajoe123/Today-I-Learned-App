package todayilearned.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import todayilearned.Submission;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/user/{username}")
public class UserProfileController {

    private SubmissionRepository submissionRepo;
    private UserRepository userRepo;

    public UserProfileController(SubmissionRepository submissionRepo, UserRepository userRepo) {
        this.submissionRepo = submissionRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String userSubmissions() {
        return "userSubmissions";
    }

    @ModelAttribute(name = "allSubmissions")
    public ArrayList<Submission> findUserSubmissions(@PathVariable("username") String username, Model model) {
        return submissionRepo.findByAuthor(userRepo.findByUsername(username));
    }

    @ModelAttribute(name = "username")
    public String getUsername(@PathVariable("username") String username) {
        return username;
    }


}
