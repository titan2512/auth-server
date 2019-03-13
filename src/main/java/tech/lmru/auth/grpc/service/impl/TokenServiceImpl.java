package tech.lmru.auth.grpc.service.impl;

import io.grpc.stub.StreamObserver;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.AccessToken;
import tech.lmru.auth.grpc.service.generated.impl.AuthorizationRequest;
import tech.lmru.auth.grpc.service.generated.impl.TokenServiceGrpc.TokenServiceImplBase;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@GRPCService
public class TokenServiceImpl extends TokenServiceImplBase {
    
   // @Inject
    TokenEndpoint tokenEndpoint;
    
    @Override
    public void getToken(AuthorizationRequest request, StreamObserver<AccessToken> responseObserver){
        
        String jti="";
		String token="";
		Principal principal = null;
		Map<String, String> parameters = new HashMap();
		try {
			ResponseEntity<OAuth2AccessToken> oathToken = tokenEndpoint.postAccessToken(principal, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		AccessToken response = AccessToken.newBuilder()
                .setJti(jti)
                .setToken(token)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
}
