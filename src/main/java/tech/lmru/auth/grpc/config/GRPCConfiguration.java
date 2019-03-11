package tech.lmru.auth.grpc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tech.lmru.auth.grpc.server.GRPCServerRunner;

@Configuration
@ConditionalOnBean(annotation = GRPCService.class)
public class GRPCConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public GRPCServerRunner grpcServerRunner() {
        return new GRPCServerRunner(Integer.valueOf(env.getProperty("grpc.port")));
    }
}
