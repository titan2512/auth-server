package tech.lmru.auth.grpc.service.impl.role;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.Role;
import tech.lmru.auth.grpc.service.generated.impl.RoleReadByIdServiceGrpc;

@GRPCService
public class RoleReadByIdServiceImpl extends RoleReadByIdServiceGrpc.RoleReadByIdServiceImplBase {

  @Override
  public void readByIdRole(EntityIdRequest request, StreamObserver<Role> responseObserver) {
    super.readByIdRole(request, responseObserver);
  }
}
