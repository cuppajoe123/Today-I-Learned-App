package todayilearned.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import todayilearned.Submission;
import todayilearned.data.SubmissionRepository;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class HomePageResults {

    private ArrayList<Submission> topSubmissions;

    @Autowired
    private SubmissionRepository submissionRepo;

    public ArrayList<Submission> getTopSubmissions() {
        return this.topSubmissions;
    }

    @Scheduled(fixedRate = 60000)
    public void refreshSubmissions() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)-3);
        log.info("Fetching results from after " + calendar.getTime());
        ArrayList<Submission> results = submissionRepo.findByPostedOnAfter(calendar.getTime());
        /* Submissions are in order if the first one has a higher score than the next */
        results.sort((s1, s2) -> {
            long s1HoursSinceSubmission = TimeUnit.HOURS.convert(Math.abs(new Date().getTime() - s1.getPostedOn().getTime()), TimeUnit.MILLISECONDS);
            long s2HoursSinceSubmission = TimeUnit.HOURS.convert(Math.abs(new Date().getTime() - s2.getPostedOn().getTime()), TimeUnit.MILLISECONDS);

            double s1Score = (s1.getPoints() - 1) / Math.pow(s1HoursSinceSubmission + 2, 1.8);
            double s2Score = (s2.getPoints() - 1) / Math.pow(s2HoursSinceSubmission + 2, 1.8);
            return Double.compare(s2Score, s1Score);
        });

        this.topSubmissions = results;
    }
}
