package tech.lmru.auth.grpc.service.impl;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.web.server.handler.ExceptionHandlingWebHandler;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.AccessToken;
import tech.lmru.auth.grpc.service.generated.impl.AuthenticationRequest;
import tech.lmru.auth.grpc.service.generated.impl.ErrorDescription;
import tech.lmru.auth.grpc.service.generated.impl.TokenServiceGrpc.TokenServiceImplBase;

import javax.inject.Inject;
import java.util.*;

@GRPCService
public class TokenServiceImpl extends TokenServiceImplBase {

    private final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Inject
    TokenEndpoint tokenEndpoint;

    @Inject
    AuthenticationManager authenticationManager;

    @Inject
    CheckTokenEndpoint checkTokenEndpoint;

    @Override
    public void getToken(AuthenticationRequest request, StreamObserver<AccessToken> responseObserver) {
        Map<String, String> parameters = new HashMap() {{
            put("client_id", request.getClientId());
            put("grant_type", request.getGrantType());
        }};
        OAuth2AccessToken accessToken = null;
        AccessToken response = null;
        AccessToken.Builder responseBuilder = AccessToken.newBuilder();
        try {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(request.getName(), request.getPass(), null);
            Authentication userAuthentication = authenticationManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            OAuth2Request oauth2Request = new OAuth2Request(parameters, request.getClientId(), null, true,
                    new HashSet<String>() {{
                        add(request.getGrantType());
                    }}, null, null, null, null);

            OAuth2Authentication principal = new OAuth2Authentication(oauth2Request, userAuthentication);
            ResponseEntity<OAuth2AccessToken> resp = tokenEndpoint.postAccessToken(principal, parameters);
            accessToken = resp.getBody();
            responseBuilder
                    .setToken(Optional.ofNullable(accessToken).map(OAuth2AccessToken::getValue).orElse(""))
                    .setJti((String) Optional.ofNullable(accessToken).map(t-> t.getAdditionalInformation().getOrDefault(
                            AccessTokenConverter.JTI, "")).orElse(""));
        }catch (AuthenticationException | UnsupportedGrantTypeException |InvalidRequestException | InvalidGrantException  | InvalidClientException e ) {
            responseBuilder.setError(ErrorDescription.newBuilder().setErrorCode("AuthError").setErrorMessage(e.getMessage()));
        } catch (Exception e) {
            responseBuilder.setError(ErrorDescription.newBuilder().setErrorCode("UnknownError").setErrorMessage(e.getMessage()));
        }finally {
            response = responseBuilder.build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void checkToken(AccessToken request, StreamObserver<AccessToken> responseObserver) {
        Map<String, ?> map = null;
        AccessToken response = null;
        AccessToken.Builder responseBuilder = AccessToken.newBuilder();
        try {
            map = checkTokenEndpoint.checkToken(request.getToken());
            responseBuilder
                    .setToken(request.getToken())
                    .setJti(request.getJti());

            logger.info(map.toString());
        }catch (InvalidTokenException e){
            responseBuilder.setError(ErrorDescription.newBuilder().setErrorCode("InvalidToken").setErrorMessage(e.getMessage()));
        }catch (Exception e){
            responseBuilder.setError(ErrorDescription.newBuilder().setErrorCode("UnknownError").setErrorMessage(e.getMessage()));
        }finally {
            response = responseBuilder.build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
