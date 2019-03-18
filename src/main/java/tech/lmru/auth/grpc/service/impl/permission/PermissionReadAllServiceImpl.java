package tech.lmru.auth.grpc.service.impl.permission;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.Empty;
import tech.lmru.auth.grpc.service.generated.impl.PermissionAllResponse;
import tech.lmru.auth.grpc.service.generated.impl.PermissionReadAllServiceGrpc;
import tech.lmru.entity.Permission;
import tech.lmru.repo.PermissionRepository;

@GRPCService
public class PermissionReadAllServiceImpl extends
    PermissionReadAllServiceGrpc.PermissionReadAllServiceImplBase {

  private final Logger logger = LoggerFactory.getLogger(PermissionReadAllServiceImpl.class);

  private PermissionRepository permissionRepository;

  @Autowired
  public PermissionReadAllServiceImpl(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

  @Override
  public void readAllPermission(Empty request,
      StreamObserver<PermissionAllResponse> responseObserver) {
    List<Permission> all = permissionRepository.findAll();

    logger.info("List size of all permission {}", all.size());
    Collection<tech.lmru.auth.grpc.service.generated.impl.Permission> permissions = new ArrayList<>();
    for (Permission permission : all
    ) {
      tech.lmru.auth.grpc.service.generated.impl.Permission permission1 = tech.lmru.auth.grpc.service.generated.impl.Permission
          .newBuilder()
          .setId(permission.getId())
          .setName(permission.getName())
          .setCode(permission.getCode())
          .build();
      permissions.add(permission1);
    }

    PermissionAllResponse response = PermissionAllResponse.newBuilder()
        .addAllPermissions(permissions).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
