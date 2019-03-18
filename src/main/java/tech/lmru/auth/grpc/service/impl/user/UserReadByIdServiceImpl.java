package tech.lmru.auth.grpc.service.impl.user;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.User;
import tech.lmru.auth.grpc.service.generated.impl.UserReadByIdServiceGrpc;

@GRPCService
public class UserReadByIdServiceImpl extends UserReadByIdServiceGrpc.UserReadByIdServiceImplBase {

  @Override
  public void readByIdUser(EntityIdRequest request, StreamObserver<User> responseObserver) {
    super.readByIdUser(request, responseObserver);
  }
}
