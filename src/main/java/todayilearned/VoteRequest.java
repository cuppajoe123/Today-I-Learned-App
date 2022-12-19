package todayilearned;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VoteRequest {

    @NotNull
    private String JSESSIONID;

    @NotNull
    private Long submissionId;
}
