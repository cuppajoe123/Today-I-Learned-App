package todayilearned.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.authorizeRequests().antMatchers("/register").denyAll();
        http.authorizeRequests().antMatchers("/submit", "/vote").hasRole("USER")
                        .antMatchers("/", "/**").permitAll();
        http.authorizeRequests().antMatchers("/register").not().hasRole("USER");
        http.csrf().ignoringAntMatchers("/h2-console/**", "/vote");
        http.headers().frameOptions().sameOrigin();
        http.headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self' jsdelivr.net *.jsdelivr.net 'unsafe-eval'");
        http.formLogin().loginPage("/login");
        http.logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passWordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
