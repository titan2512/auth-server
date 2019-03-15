package tech.lmru.auth.grpc.service.impl.user;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityDeleteResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.UserDeleteByIdServiceGrpc;

@GRPCService
public class UserDeleteByIdServiceImpl extends
    UserDeleteByIdServiceGrpc.UserDeleteByIdServiceImplBase {

  @Override
  public void deleteByIdUser(EntityIdRequest request,
      StreamObserver<EntityDeleteResponse> responseObserver) {
    super.deleteByIdUser(request, responseObserver);
  }
}
