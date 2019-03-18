package tech.lmru.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import tech.lmru.auth.jwt.converter.JwtAccessWithUserCredentialTokenConverter;

import javax.sql.DataSource;

/**
 * Created by Ilya on 06.03.2019.
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ApplicationProperties applicationProperties;

    //@Autowired
    //@Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("service-ui")
                .secret("service-ui-pass")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("read", "write")
                .accessTokenValiditySeconds(applicationProperties.getTokenServiceProp().getTokenValidSecond());
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenServices(tokenServices())
        .tokenStore(tokenStore()).authenticationManager(authenticationManager);
    }

    //@Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(tokenStore());
        services.setTokenEnhancer(tokenEnhancer());
        services.setClientDetailsService(clientDetailsService);
        return services;
    }


    @Bean
    public JwtAccessWithUserCredentialTokenConverter tokenEnhancer(){
        return new JwtAccessWithUserCredentialTokenConverter();
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
