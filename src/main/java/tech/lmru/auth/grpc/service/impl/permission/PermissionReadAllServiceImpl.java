package tech.lmru.auth.grpc.service.impl.permission;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.Empty;
import tech.lmru.auth.grpc.service.generated.impl.PermissionAllResponse;
import tech.lmru.auth.grpc.service.generated.impl.PermissionReadAllServiceGrpc;

@GRPCService
public class PermissionReadAllServiceImpl extends
    PermissionReadAllServiceGrpc.PermissionReadAllServiceImplBase {

  @Override
  public void readAllPermission(Empty request,
      StreamObserver<PermissionAllResponse> responseObserver) {
    super.readAllPermission(request, responseObserver);
  }
}
