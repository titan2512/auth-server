package tech.lmru.auth.ldap.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Service;
import tech.lmru.auth.config.ApplicationProperties;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.Collection;

@Service("userDetailsContextMapper")
public class ActiveDirectoryUserDetailsContextMapper implements UserDetailsContextMapper {

    private final Logger logger = LoggerFactory.getLogger(ActiveDirectoryUserDetailsContextMapper.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String s, Collection<? extends GrantedAuthority> collection) {
        Attributes attributes = ctx.getAttributes();
        String nameAttr = applicationProperties.getLdap().getNameAttribute();
        try {
            String fullname = attributes.get(nameAttr) == null ? "" : attributes.get(nameAttr).get().toString();
            return userDetailsService.loadUserByUsername(fullname);
        } catch (NamingException exc) {
            logger.error("Can't exctract attribute {}' from AD user context", nameAttr);
            throw new UsernameNotFoundException("Can't get name user");
        }
    }

    @Override
    public void mapUserToContext(UserDetails userDetails, DirContextAdapter dirContextAdapter) {

    }
}
