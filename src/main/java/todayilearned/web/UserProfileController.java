package todayilearned.web;

import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import todayilearned.model.Submission;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;

import java.util.ArrayList;

@Controller
@RequestMapping("/user/{username}")
@Slf4j
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

    @GetMapping("/rss")
    public @ResponseBody String getRssFeed(@PathVariable String username) {
        SyndFeedImpl feed =  userRepo.findByUsername(username).getRssFeed();
        SyndFeedOutput output = new SyndFeedOutput();
        try {
            return output.outputString(feed);
        } catch (FeedException e) {
            log.error("RSS Feed Exception: " + e);
        }
        return "RSS Feed Error";
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
