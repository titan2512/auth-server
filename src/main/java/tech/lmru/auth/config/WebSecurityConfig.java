package tech.lmru.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

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
    private UserDetailsContextMapper userDetailsContextMapper;

    @Override
    public void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        if (applicationProperties.getLdap().isEnable()) {
            auth=auth.authenticationProvider(createLDAPAuthenticationProvider());
        }
        auth.inMemoryAuthentication()
                .withUser("testUser").password(passwordEncoder.encode("123"))
                .roles("USER")
                .authorities(new SimpleGrantedAuthority("test_cred1"), new SimpleGrantedAuthority("test_cred2"))
                .and()
                .and()
                .userDetailsService(userDetailsService);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public AuthenticationProvider createLDAPAuthenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider provider =
                new ActiveDirectoryLdapAuthenticationProvider(applicationProperties.getLdap().getDomain(), applicationProperties.getLdap().getUrl());
        provider.setConvertSubErrorCodesToExceptions(true);
        provider.setUseAuthenticationRequestCredentials(true);
        provider.setUserDetailsContextMapper(userDetailsContextMapper);
        return provider;
    }
}
