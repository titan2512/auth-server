package tech.lmru.auth.grpc.service.impl.role;

import io.grpc.stub.StreamObserver;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.Empty;
import tech.lmru.auth.grpc.service.generated.impl.RoleAllResponse;
import tech.lmru.auth.grpc.service.generated.impl.RoleReadAllServiceGrpc;

@GRPCService
public class RoleReadAllServiceImpl extends RoleReadAllServiceGrpc.RoleReadAllServiceImplBase {

  @Override
  public void readAllRole(Empty request, StreamObserver<RoleAllResponse> responseObserver) {
    super.readAllRole(request, responseObserver);
  }
}
