package todayilearned.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Data
@NoArgsConstructor
public class Submission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User author;

    private LocalDate postedOn = LocalDate.now();

    /* UNIX timestamp for Algolia */
    private Long timestamp = Instant.now().getEpochSecond();

    @NotBlank(message = "A title is required")
    private String title;

    @NotBlank(message = "A body is required")
    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Column(name = "htmlBody", columnDefinition = "text")
    private String htmlBody;

    private Long points = 0L;

    /* Constructor without id */
    public Submission(User author, LocalDateTime postedOn, String title, String body, String htmlBody) {
        this.author = author;
        this.postedOn = postedOn.toLocalDate();
        this.timestamp = postedOn.toEpochSecond(ZoneOffset.UTC);
        this.title = title;
        this.body = body;
        this.htmlBody = htmlBody;
    }

    /* Constructor with id, for tests */
    public Submission(Long id, User author, LocalDateTime postedOn, String title, String body, String htmlBody) {
        this.id = id;
        this.author = author;
        this.postedOn = postedOn.toLocalDate();
        this.timestamp = postedOn.toEpochSecond(ZoneOffset.UTC);
        this.title = title;
        this.body = body;
        this.htmlBody = htmlBody;
    }

    public void incrementPoints() {
        this.points++;
    }

    public AlgoliaSubmission convertToAlgoliaSubmission() {
        return new AlgoliaSubmission(this.id, this.author.getUsername(), this.postedOn, this.title, this.body, this.points);
    }

}
