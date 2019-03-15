package tech.lmru.auth.grpc.service.impl.permission;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityCreateResponse;
import tech.lmru.auth.grpc.service.generated.impl.Permission;
import tech.lmru.auth.grpc.service.generated.impl.PermissionCreateOrUpdateServiceGrpc;

@GRPCService
public class PermissionCreateOrUpdateServiceImpl extends
    PermissionCreateOrUpdateServiceGrpc.PermissionCreateOrUpdateServiceImplBase {

  @Override
  public void createOrUpdatePermission(Permission request,
      StreamObserver<EntityCreateResponse> responseObserver) {
    super.createOrUpdatePermission(request, responseObserver);
  }
}
