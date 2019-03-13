package tech.lmru.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.server.GRPCServerRunner;

@Configuration
@ConditionalOnBean(annotation = GRPCService.class)
public class GRPCConfiguration {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public GRPCServerRunner grpcServerRunner() {
        return new GRPCServerRunner(applicationProperties.getGrpc().getPort());
    }
}
