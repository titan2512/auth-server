package tech.lmru.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Ilya on 07.03.2019.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userDnPatterns(applicationProperties.getLdap().getUserDnPattern())
              //  .groupSearchBase(applicationProperties.getLdap().getGroupSearchBase())
                .contextSource()
                    .url(applicationProperties.getLdap().getUrl())
                    .and()
                .passwordCompare()
                    .passwordEncoder(passwordEncoder)
                    //.passwordAttribute(applicationProperties.getLdap().getPasswordAttribute())
                    .and()
                .and()
                .userDetailsService(userDetailsService)
                .and()
                .inMemoryAuthentication()
                .withUser("testUser").password(passwordEncoder.encode("123"))
                .roles("USER")
                .authorities(new SimpleGrantedAuthority("test_cred1"), new SimpleGrantedAuthority("test_cred2"));
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
