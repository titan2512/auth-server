package tech.lmru.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "auth-server", ignoreInvalidFields = true)
public class ApplicationProperties {

    private final Grpc grpc= new Grpc();
    private final Jdbc jdbc = new Jdbc();

    @Data
    public static class Grpc{

        private int port;
    }

    @Data
    public static class Jdbc{

        private String driverClassName;
        private String url;
        private String user;
        private String password;
    }
}
