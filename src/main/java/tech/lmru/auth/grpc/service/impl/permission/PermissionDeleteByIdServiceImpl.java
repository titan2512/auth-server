package tech.lmru.auth.grpc.service.impl.permission;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityDeleteResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.PermissionDeleteByIdServiceGrpc;
import tech.lmru.repo.PermissionRepository;

@GRPCService
public class PermissionDeleteByIdServiceImpl extends
    PermissionDeleteByIdServiceGrpc.PermissionDeleteByIdServiceImplBase {

  private PermissionRepository permissionRepository;

  @Autowired
  public PermissionDeleteByIdServiceImpl(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

  @Override
  public void deleteByIdPermission(EntityIdRequest request,
      StreamObserver<EntityDeleteResponse> responseObserver) {
    Integer id = Integer.valueOf(request.getId());

    EntityDeleteResponse entityDeleteResponse = null;

    try {
      permissionRepository.deleteById(id);
      entityDeleteResponse = EntityDeleteResponse.newBuilder().setSuccess(true).build();
    } catch (Exception e) {
      entityDeleteResponse = EntityDeleteResponse.newBuilder().setSuccess(false).build();
    } finally {
      responseObserver.onNext(entityDeleteResponse);
      responseObserver.onCompleted();
    }
  }
}
