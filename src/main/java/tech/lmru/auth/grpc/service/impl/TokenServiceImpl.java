package tech.lmru.auth.grpc.service.impl;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.service.generated.impl.AccessToken;
import tech.lmru.auth.grpc.service.generated.impl.AuthorizationRequest;
import tech.lmru.auth.grpc.service.generated.impl.TokenServiceGrpc.TokenServiceImplBase;

public class TokenServiceImpl extends TokenServiceImplBase {
    
    @Inject 
    TokenEndpoint tokenEndpoint;
    
    @Override
    public void getToken(AuthorizationRequest request, StreamObserver<AccessToken> responseObserver){
        
        String jti="";
		String token="";
		Principal principal = null;
		Map<String, String> parameters = new HashMap();
		try {
			ResponseEntity<OAuth2AccessToken> oathToken = tokenEndpoint.postAccessToken(principal, parameters);
		} catch (HttpRequestMethodNotSupportedException e) {
			// TODO Auto-generated catch block
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
