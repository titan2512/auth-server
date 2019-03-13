package tech.lmru.auth.grpc.service.impl;

import io.grpc.stub.StreamObserver;
import org.springframework.web.bind.support.SessionStatus;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.AuthorizationRequest;
import tech.lmru.auth.grpc.service.generated.impl.AuthorizationResponse;
import tech.lmru.auth.grpc.service.generated.impl.AuthorizeServiceGrpc;

import java.security.Principal;
import java.util.Map;


/**
 * Created by Ilya on 10.03.2019.
 */

@GRPCService
public class AuthorizationServiceImpl extends AuthorizeServiceGrpc.AuthorizeServiceImplBase {

    @Override
    public void authorize(AuthorizationRequest request, StreamObserver<AuthorizationResponse> responseObserver) {
        Map<String, Object> model = null;
        Map<String, String> parameters =null;
        SessionStatus sessionStatus = null;
        Principal principal = null;
        //endpoint.authorize(model, parameters, sessionStatus, principal);
        AuthorizationResponse response = AuthorizationResponse.newBuilder()
                .setAuthorize(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    
}
