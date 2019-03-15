package tech.lmru.auth.grpc.service.impl.permission;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.Permission;
import tech.lmru.auth.grpc.service.generated.impl.PermissionReadByIdServiceGrpc;

@GRPCService
public class PermissionReadByIdServiceImpl extends
    PermissionReadByIdServiceGrpc.PermissionReadByIdServiceImplBase {

  @Override
  public void readByIdRole(EntityIdRequest request, StreamObserver<Permission> responseObserver) {
    super.readByIdRole(request, responseObserver);
  }
}
