package todayilearned.model;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.SerializationUtils;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "\"USER\"")
@Data
@NoArgsConstructor(access= AccessLevel.PRIVATE, force = true)
@Slf4j
//@AllArgsConstructor
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean enabled = false;
    private final String email;
    private final String username;
    private final String password;

    @Column(columnDefinition = "bytea")
    private byte[] rssFeed;

    /* A list of the id's of submissions the user has voted on */
    private ArrayList<Long> votedSubmissions = new ArrayList<>();

    /* For testing purposes */
    public User(Long id, String email, String username, String password, boolean enabled) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.enabled = enabled;

        SyndFeedImpl feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle(this.username);
        feed.setLink("http://localhost:8080/user/" + this.username);
        feed.setDescription("Latest posts from " + this.username);
        feed.setAuthor(this.username);
        this.rssFeed = SerializationUtils.serialize(feed);
    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;

        SyndFeedImpl feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle(this.username);
        feed.setLink("http://localhost:8080/user/" + this.username);
        feed.setDescription("Latest posts from " + this.username);
        feed.setAuthor(this.username);
        this.rssFeed = SerializationUtils.serialize(feed);
    }



    public void addSubmission(Long id) {
        this.votedSubmissions.add(id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
