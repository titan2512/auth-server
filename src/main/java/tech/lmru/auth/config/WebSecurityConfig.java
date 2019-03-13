package tech.lmru.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by Ilya on 07.03.2019.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //@Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("testUser").password("123").roles("USER")
                .authorities(new SimpleGrantedAuthority("test_auth"));
    }

    //@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    protected void configure(HttpSecurity http) throws Exception {

        http.anonymous().disable()
                .antMatcher("/oauth/token")
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/token")).disable()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //@Bean
    protected AccessDeniedHandler accessDeniedHandler() {
        return new OAuth2AccessDeniedHandler();
    }

    //@Bean
    protected AuthenticationEntryPoint authenticationEntryPoint() {
        OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
        entryPoint.setTypeName("Basic");
        entryPoint.setRealmName("oauth2/client");
        return entryPoint;
    }
}
