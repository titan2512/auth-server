package tech.lmru.auth.grpc.service.impl;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.AccessToken;
import tech.lmru.auth.grpc.service.generated.impl.AuthenticationRequest;
import tech.lmru.auth.grpc.service.generated.impl.TokenServiceGrpc.TokenServiceImplBase;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@GRPCService
public class TokenServiceImpl extends TokenServiceImplBase {
    
    @Inject
    TokenEndpoint tokenEndpoint;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public void getToken(AuthenticationRequest request, StreamObserver<AccessToken> responseObserver){
		Map<String, String> parameters = new HashMap(){{
		    put("client_id", request.getClientId());
		    put("grant_type", request.getGrantType());
		}};
        OAuth2AccessToken accessToken = null;
		try {

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(request.getName(), request.getPass(), null);
            OAuth2Request oaut2Request = new OAuth2Request(parameters, request.getClientId(), null, true,
                    new HashSet<String>(){{add(request.getGrantType());}} ,
                    null, null, null, null);

            OAuth2Authentication principal =new OAuth2Authentication(oaut2Request , authenticationManager.authenticate(auth));
            ResponseEntity<OAuth2AccessToken> resp = tokenEndpoint.postAccessToken(principal, parameters);
            accessToken = resp.getBody();
            System.out.println(accessToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		AccessToken response = AccessToken.newBuilder()
                .setToken(accessToken != null ? accessToken.getValue() : "")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}
