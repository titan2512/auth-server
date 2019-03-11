package tech.lmru.auth.grpc.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.type.StandardMethodMetadata;
import tech.lmru.auth.grpc.config.GRPCService;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class GRPCServerRunner implements CommandLineRunner, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(GRPCServerRunner.class);

    @Autowired
    private AbstractApplicationContext applicationContext;

    private final int port;
    private Server server;
    private final ServerBuilder<?> serverBuilder;

    public GRPCServerRunner(int port) {
        this.port = port;
        serverBuilder = ServerBuilder
                .forPort(port);
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(()->{
            try {
                GRPCServerRunner.this.server.awaitTermination();
            } catch (InterruptedException e) {
                logger.error("gRPC server stopped.", e);
            }
        });
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void destroy() throws Exception {

        logger.info("Shutting down gRPC server ...");
        Optional.ofNullable(server).ifPresent(Server::shutdown);
        logger.info("gRPC server stopped.");
    }

    @Override
    public void run(String... strings) throws Exception {

        logger.info("Starting gRPC server ...");

        getBeanNamesByTypeWithAnnotation(GRPCService.class, BindableService.class)
                .forEach(name -> {
                    BindableService srv = applicationContext.getBeanFactory().getBean(name, BindableService.class);
                    ServerServiceDefinition serviceDefinition = srv.bindService();
                    serverBuilder.addService(serviceDefinition);

                    logger.info("'{}' service has been registered.", srv.getClass().getName());
                });
        server = serverBuilder.build().start();

        logger.info("gRPC server started, listening on port {}.", port);

        startDaemonAwaitThread();
    }

    private <T> Stream<String> getBeanNamesByTypeWithAnnotation(Class<? extends Annotation> annotationType, Class<T> beanType) throws Exception {

        return Stream.of(applicationContext.getBeanNamesForType(beanType))
                .filter(name -> {
                    final BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition(name);
                    final Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(annotationType);

                    if (!beansWithAnnotation.isEmpty()) {
                        return beansWithAnnotation.containsKey(name);
                    } else if (beanDefinition.getSource() instanceof StandardMethodMetadata) {
                        StandardMethodMetadata metadata = (StandardMethodMetadata) beanDefinition.getSource();
                        return metadata.isAnnotated(annotationType.getName());
                    }

                    return false;
                });
    }
}
