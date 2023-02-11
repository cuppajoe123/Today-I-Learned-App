package todayilearned.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
public class AlgoliaSubmission implements Serializable {

    private Long objectID;

    private String author;

    private LocalDate postedOn;

    private Long timestamp;

    private String title;

    private String body;

    private Long points;

    /* Constructor with objectID, for tests */
    public AlgoliaSubmission(Long objectID, String author, LocalDate postedOn, String title, String body, Long points) {
        this.objectID = objectID;
        this.author = author;
        this.postedOn = postedOn;
        this.timestamp = postedOn.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC);
        this.title = title;
        this.body = body;
        this.points = points;
    }
}
