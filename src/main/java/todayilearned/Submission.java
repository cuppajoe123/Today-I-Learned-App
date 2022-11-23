package todayilearned;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
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
    private String body;

    public Submission(User author, Date postedOn, String title, String body) {
        this.author = author;
        this.postedOn = postedOn;
        this.title = title;
        this.body = body;
    }

}
