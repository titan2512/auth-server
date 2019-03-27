package tech.lmru.auth.grpc.service.impl;

import io.grpc.stub.StreamObserver;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.Empty;
import tech.lmru.auth.grpc.service.generated.impl.EntityCreateResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityDeleteResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.Role;
import tech.lmru.auth.grpc.service.generated.impl.RoleAllResponse;
import tech.lmru.auth.grpc.service.generated.impl.RoleServiceGrpc;
import tech.lmru.entity.Permission;
import tech.lmru.repo.RoleRepository;

@GRPCService
public class RoleServiceImpl extends
    RoleServiceGrpc.RoleServiceImplBase {

  private RoleRepository roleRepository;

  @Autowired
  public RoleServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public void createOrUpdateRole(Role request,
      StreamObserver<EntityCreateResponse> responseObserver) {

    tech.lmru.entity.Role role = new tech.lmru.entity.Role();
    role.setId(request.getId());
    role.setCode(request.getCode());
    role.setName(request.getName());
    List<Permission> collect = request.getPermissionsList().stream()
        .map(permission ->
        {
          Permission permissionDto = new Permission();
          permissionDto.setId(permission.getId());
          permissionDto.setName(permission.getName());
          permissionDto.setCode(permission.getCode());
          return permissionDto;
        }).collect(Collectors.toList());
    role.setPermissions(collect);
    tech.lmru.entity.Role save = roleRepository.save(role);
    EntityCreateResponse response = EntityCreateResponse.newBuilder().setId(save.getId()).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();

  }

  @Override
  public void readAllRole(Empty request, StreamObserver<RoleAllResponse> responseObserver) {
    List<tech.lmru.entity.Role> all = roleRepository.findAll();
    RoleAllResponse response = null;
    if (all.isEmpty()) {
      response = RoleAllResponse.newBuilder()
          .build();
    } else {
      List<tech.lmru.auth.grpc.service.generated.impl.Role> collect = all.stream().map(role -> {
        tech.lmru.auth.grpc.service.generated.impl.Role roleImpl = tech.lmru.auth.grpc.service.generated.impl.Role
            .newBuilder()
            .setId(role.getId())
            .setCode(role.getCode())
            .setName(role.getName())
            .addAllPermissions(role.getPermissions().stream().map(permission -> {
              tech.lmru.auth.grpc.service.generated.impl.Permission permission1 = tech.lmru.auth.grpc.service.generated.impl.Permission
                  .newBuilder()
                  .setId(permission.getId())
                  .setCode(permission.getCode())
                  .setName(permission.getName()).build();
              return permission1;
            }).collect(Collectors.toList()))
            .build();
        return roleImpl;
      }).collect(Collectors.toList());
      response = RoleAllResponse.newBuilder()
          .addAllRoles(collect).build();
    }

    responseObserver.onNext(response);
    responseObserver.onCompleted();

  }


  @Override
  public void readByIdRole(EntityIdRequest request, StreamObserver<Role> responseObserver) {

    Integer id = Integer.valueOf(request.getId());
    Optional<tech.lmru.entity.Role> byId = roleRepository.findById(id);

    if (byId.isPresent()) {

      tech.lmru.entity.Role role1 = byId.get();
      List<Permission> permissions = role1.getPermissions();

      Collection<tech.lmru.auth.grpc.service.generated.impl.Permission> collection = permissions
          .stream()
          .map(p -> tech.lmru.auth.grpc.service.generated.impl.Permission.newBuilder()
              .setId(p.getId())
              .setCode(p.getCode())
              .setName(p.getName()).build()).collect(Collectors.toList());

      Role role = Role.newBuilder()
          .setId(role1.getId())
          .setCode(role1.getCode())
          .setName(role1.getName())
          .addAllPermissions(collection).build();

      responseObserver.onNext(role);
      responseObserver.onCompleted();
    } else {
      responseObserver.onCompleted();
      throw new EntityNotFoundException();
    }
  }

  @Override
  public void deleteByIdRole(EntityIdRequest request,
      StreamObserver<EntityDeleteResponse> responseObserver) {

    Integer id = Integer.valueOf(request.getId());

    EntityDeleteResponse entityDeleteResponse = null;
    try {
      roleRepository.deleteById(id);
      entityDeleteResponse = EntityDeleteResponse.newBuilder().setSuccess(true).build();
    } catch (Exception e) {
      entityDeleteResponse = EntityDeleteResponse.newBuilder().setSuccess(false).build();
    } finally {
      responseObserver.onNext(entityDeleteResponse);
      responseObserver.onCompleted();
    }
  }

}
