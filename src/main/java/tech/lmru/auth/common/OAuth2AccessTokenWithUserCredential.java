package tech.lmru.auth.common;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Collection;

/**
 * Created by Ilya on 17.03.2019.
 */
@Data
public class OAuth2AccessTokenWithUserCredential extends DefaultOAuth2AccessToken {

    private Collection<GrantedAuthority> userAuthorities;

    public OAuth2AccessTokenWithUserCredential(String value) {
        super(value);
    }

    public OAuth2AccessTokenWithUserCredential(OAuth2AccessToken accessToken, Collection<GrantedAuthority> userAuthorities) {
        super(accessToken);
        this.userAuthorities = userAuthorities;
    }
}
