package tech.lmru.auth.grpc.service.impl.permission;

import io.grpc.stub.StreamObserver;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.Permission;
import tech.lmru.auth.grpc.service.generated.impl.Permission.Builder;
import tech.lmru.auth.grpc.service.generated.impl.PermissionReadByIdServiceGrpc;
import tech.lmru.repo.PermissionRepository;

@GRPCService
public class PermissionReadByIdServiceImpl extends
    PermissionReadByIdServiceGrpc.PermissionReadByIdServiceImplBase {

  private PermissionRepository permissionRepository;

  @Autowired
  public PermissionReadByIdServiceImpl(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

  @Override
  public void readByIdPermission(EntityIdRequest request,
      StreamObserver<Permission> responseObserver) {
    Integer id = Integer.valueOf(request.getId());
    if (id == null) {
      throw new IllegalArgumentException();
    }
    Optional<tech.lmru.entity.Permission> byId = permissionRepository.findById(id);
    if (!byId.isPresent()) {
      throw new EntityNotFoundException();
    }

    tech.lmru.entity.Permission permission = byId.get();

    Builder builder = Permission.newBuilder()
        .setId(permission.getId())
        .setCode(permission.getCode())
        .setName(permission.getName());

    Permission response = builder.build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();

  }
}
