package tech.lmru.auth.grpc.service.impl;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.Empty;
import tech.lmru.auth.grpc.service.generated.impl.EntityCreateResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityDeleteResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.Permission;
import tech.lmru.auth.grpc.service.generated.impl.Permission.Builder;
import tech.lmru.auth.grpc.service.generated.impl.PermissionAllResponse;
import tech.lmru.auth.grpc.service.generated.impl.PermissionServiceGrpc;
import tech.lmru.repo.PermissionRepository;

@GRPCService
public class PermissionServiceImpl extends
    PermissionServiceGrpc.PermissionServiceImplBase {

  private final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

  private PermissionRepository permissionRepository;

  @Autowired
  public PermissionServiceImpl(PermissionRepository permissionRepository) {
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

  @Override
  public void readAllPermission(Empty request,
      StreamObserver<PermissionAllResponse> responseObserver) {
    List<tech.lmru.entity.Permission> all = permissionRepository.findAll();

    logger.info("List size of all permission {}", all.size());
    Collection<Permission> permissions = new ArrayList<>();
    for (tech.lmru.entity.Permission permission : all
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
