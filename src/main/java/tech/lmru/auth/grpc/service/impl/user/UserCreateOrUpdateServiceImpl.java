package tech.lmru.auth.grpc.service.impl.user;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityCreateResponse;
import tech.lmru.auth.grpc.service.generated.impl.User;
import tech.lmru.auth.grpc.service.generated.impl.UserCreateOrUpdateServiceGrpc;

@GRPCService
public class UserCreateOrUpdateServiceImpl extends
    UserCreateOrUpdateServiceGrpc.UserCreateOrUpdateServiceImplBase {

  @Override
  public void createOrUpdateUser(User request,
      StreamObserver<EntityCreateResponse> responseObserver) {
    super.createOrUpdateUser(request, responseObserver);
  }
}
