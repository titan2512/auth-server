package tech.lmru.auth.grpc.service.impl.role;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityDeleteResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.RoleDeleteByIdServiceGrpc;

@GRPCService
public class RoleDeleteByIdServiceImpl extends
    RoleDeleteByIdServiceGrpc.RoleDeleteByIdServiceImplBase {

  @Override
  public void deleteByIdRole(EntityIdRequest request,
      StreamObserver<EntityDeleteResponse> responseObserver) {
    super.deleteByIdRole(request, responseObserver);
  }
}
