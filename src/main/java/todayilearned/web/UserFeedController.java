package todayilearned.web;

import org.springframework.data.annotation.LastModifiedDate;
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

@Controller
@RequestMapping("/user/{username}")
public class UserFeedController {

    private SubmissionRepository submissionRepo;
    private UserRepository userRepo;

    public UserFeedController(SubmissionRepository submissionRepo, UserRepository userRepo) {
        this.submissionRepo = submissionRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String userPosts() {
        return "userPosts";
    }

    @ModelAttribute(name = "submissions")
    public ArrayList<Submission> findUserSubmissions(@PathVariable("username") String username, Model model) {
        return submissionRepo.findByAuthor(userRepo.findByUsername(username));
    }

    @ModelAttribute(name = "username")
    public String getUsername(@PathVariable("username") String username) {
        return username;
    }


}
