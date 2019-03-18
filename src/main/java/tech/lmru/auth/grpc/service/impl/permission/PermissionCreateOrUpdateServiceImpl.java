package tech.lmru.auth.grpc.service.impl.permission;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityCreateResponse;
import tech.lmru.auth.grpc.service.generated.impl.Permission;
import tech.lmru.auth.grpc.service.generated.impl.PermissionCreateOrUpdateServiceGrpc;
import tech.lmru.repo.PermissionRepository;

@GRPCService
public class PermissionCreateOrUpdateServiceImpl extends
    PermissionCreateOrUpdateServiceGrpc.PermissionCreateOrUpdateServiceImplBase {

  private final Logger logger = LoggerFactory.getLogger(PermissionCreateOrUpdateServiceImpl.class);

  private PermissionRepository permissionRepository;

  @Autowired
  public PermissionCreateOrUpdateServiceImpl(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

  @Override
  public void createOrUpdatePermission(Permission request,
      StreamObserver<EntityCreateResponse> responseObserver) {

    tech.lmru.entity.Permission permission = new tech.lmru.entity.Permission();
    permission.setId(request.getId());
    permission.setCode(request.getCode());
    permission.setName(request.getName());

    tech.lmru.entity.Permission save = permissionRepository.save(permission);

    EntityCreateResponse entityCreateResponse = EntityCreateResponse
        .newBuilder()
        .setId(save.getId())
        .build();

    responseObserver.onNext(entityCreateResponse);
    responseObserver.onCompleted();

  }
}
