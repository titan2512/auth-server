package tech.lmru.auth.grpc.service.impl.user;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.Empty;
import tech.lmru.auth.grpc.service.generated.impl.UserAllResponse;
import tech.lmru.auth.grpc.service.generated.impl.UserReadAllServiceGrpc;

@GRPCService
public class UserReadAllServiceImpl extends UserReadAllServiceGrpc.UserReadAllServiceImplBase {

  @Override
  public void readAllUser(Empty request, StreamObserver<UserAllResponse> responseObserver) {
    super.readAllUser(request, responseObserver);
  }
}
