package tech.lmru.auth.grpc.service.impl.role;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityCreateResponse;
import tech.lmru.auth.grpc.service.generated.impl.Role;
import tech.lmru.auth.grpc.service.generated.impl.RoleCreateOrUpdateServiceGrpc;

@GRPCService
public class RoleCreateOrUpdateServiceImpl extends
    RoleCreateOrUpdateServiceGrpc.RoleCreateOrUpdateServiceImplBase {

  @Override
  public void createOrUpdateRole(Role request,
      StreamObserver<EntityCreateResponse> responseObserver) {
    super.createOrUpdateRole(request, responseObserver);
  }
}
