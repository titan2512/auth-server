package tech.lmru.auth.oauth2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.lmru.entity.Role;
import tech.lmru.entity.User;
import tech.lmru.repo.UserRepository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * Created by Ilya on 18.03.2019.
 */
@Service("userDetailsService")
@Transactional
public class DataBaseUserDetailsService implements UserDetailsService {

    @Inject
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(login)){
            throw new UsernameNotFoundException("Login must by no null");
        }
        User user = userRepository.findByCode(login);
        if (user==null) {
            throw new UsernameNotFoundException(String.format("User with login %s not found", login));
        }
        return new org.springframework.security.core.userdetails.User(login, passwordEncoder.encode("pass"), true, true, true, true,
                getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        if (!CollectionUtils.isEmpty(roles)) {
            return roles.stream().flatMap(r -> r.getPermissions().stream()).distinct().map(p -> new SimpleGrantedAuthority(p.getCode())).collect(toList());
        }
        return Collections.emptyList();
    }
}
