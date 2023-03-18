package todayilearned.web;

import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;
import todayilearned.model.Submission;
import todayilearned.data.SubmissionRepository;
import todayilearned.data.UserRepository;
import todayilearned.model.User;

import java.util.ArrayList;
import java.util.Optional;

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

    @GetMapping(value = "/rss", produces= MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody String getRssFeed(@PathVariable String username) {
        SyndFeedImpl feed;
        Optional<User> userOptional = userRepo.findByUsername(username);
        if (userOptional.isPresent())
            feed = (SyndFeedImpl) SerializationUtils.deserialize(userOptional.get().getRssFeed());
        else {
            log.error("User " + username + " does not exist");
            return "User " + username + " does not exist";
        }

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
        Optional<User> userOptional = userRepo.findByUsername(username);
        if (userOptional.isPresent())
            return submissionRepo.findByAuthor(userOptional.get());
        else {
            log.error("User " + username + " does not exist");
            return new ArrayList<>();
        }
    }

    @ModelAttribute(name = "username")
    public String getUsername(@PathVariable("username") String username) {
        return username;
    }


}
