package tech.lmru.auth.grpc.service.impl.permission;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityDeleteResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.PermissionDeleteByIdServiceGrpc;

@GRPCService
public class PermissionDeleteByIdServiceImpl extends
    PermissionDeleteByIdServiceGrpc.PermissionDeleteByIdServiceImplBase {

  @Override
  public void deleteByIdPermission(EntityIdRequest request,
      StreamObserver<EntityDeleteResponse> responseObserver) {
    super.deleteByIdPermission(request, responseObserver);
  }
}
