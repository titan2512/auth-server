package tech.lmru.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * Created by Ilya on 06.03.2019.
 */
@Configuration
//@EnableAuthorizationServer
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ApplicationProperties applicationProperties;

   // @Autowired
   // @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

   // @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        //oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

   // @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("service-ui")
                .secret("service-ui-pass")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .accessTokenValiditySeconds(60);
    }

    //@Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenServices(tokenServices());
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
    }


    //@Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(tokenStore());
        services.setTokenEnhancer(tokenEnhancer());
        services.setSupportRefreshToken(true);
        return services;
    }

    //@Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
}

   // @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        return new JwtAccessTokenConverter();
    }

   // @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        return initializer;
    }


   // @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(applicationProperties.getJdbc().getDriverClassName());
        dataSource.setUrl(applicationProperties.getJdbc().getUrl());
        dataSource.setUsername(applicationProperties.getJdbc().getUser());
        dataSource.setPassword(applicationProperties.getJdbc().getPassword());
        return dataSource;
    }

}
