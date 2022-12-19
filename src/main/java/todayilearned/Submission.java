package todayilearned;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class Submission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User author;

    private Date postedOn = new Date();

    @NotBlank(message = "A title is required")
    private String title;

    @NotBlank(message = "A body is required")
    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Column(name = "htmlBody", columnDefinition = "text")
    private String htmlBody;

    private Long points = 0L;

    /* Constructor without id */
    public Submission(User author, Date postedOn, String title, String body, String htmlBody) {
        this.author = author;
        this.postedOn = postedOn;
        this.title = title;
        this.body = body;
        this.htmlBody = htmlBody;
    }

    /* Constructor with id, for tests */
    public Submission(Long id, User author, Date postedOn, String title, String body, String htmlBody) {
        this.id = id;
        this.author = author;
        this.postedOn = postedOn;
        this.title = title;
        this.body = body;
        this.htmlBody = htmlBody;
    }

    public void incrementPoints() {
        this.points++;
    }

}
