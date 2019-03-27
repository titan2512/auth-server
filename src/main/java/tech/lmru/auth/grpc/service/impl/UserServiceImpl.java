package tech.lmru.auth.grpc.service.impl;

import io.grpc.stub.StreamObserver;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.Empty;
import tech.lmru.auth.grpc.service.generated.impl.EntityCreateResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityDeleteResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.User;
import tech.lmru.auth.grpc.service.generated.impl.UserAllResponse;
import tech.lmru.auth.grpc.service.generated.impl.UserServiceGrpc;
import tech.lmru.entity.Permission;
import tech.lmru.entity.Role;
import tech.lmru.repo.UserRepository;

@GRPCService
public class UserServiceImpl extends
    UserServiceGrpc.UserServiceImplBase {

  private UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void createOrUpdateUser(User request,
      StreamObserver<EntityCreateResponse> responseObserver) {
    tech.lmru.entity.User user = new tech.lmru.entity.User();
    user.setId(request.getId());
    user.setCode(request.getCode());
    user.setName(request.getName());
    user.setPassword(request.getPassword());

    List<Role> collect = request.getRolesList().stream().map(role -> {
      Role roleEntity = new Role();
      roleEntity.setId(role.getId());
      roleEntity.setCode(role.getCode());
      roleEntity.setName(role.getName());
      roleEntity.setPermissions(role.getPermissionsList().stream().map(permission -> {
        Permission permissionEntity = new Permission();
        permissionEntity.setId(permission.getId());
        permissionEntity.setCode(permission.getCode());
        permissionEntity.setName(permission.getName());
        return permissionEntity;
      }).collect(Collectors.toList()));
      return roleEntity;
    }).collect(Collectors.toList());

    user.setRoles(new HashSet<>(collect));
    tech.lmru.entity.User save = userRepository.save(user);
    EntityCreateResponse response = EntityCreateResponse.newBuilder().setId(save.getId()).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();

  }

  @Override
  public void readAllUser(Empty request, StreamObserver<UserAllResponse> responseObserver) {

    List<tech.lmru.entity.User> allUsers = userRepository.findAll();
    UserAllResponse userAllResponse = null;
    if (allUsers.isEmpty()) {
      userAllResponse = UserAllResponse.newBuilder().build();
    } else {
      List<tech.lmru.auth.grpc.service.generated.impl.User> collect = allUsers.stream()
          .map(user -> {
            return tech.lmru.auth.grpc.service.generated.impl.User.newBuilder()
                .setId(user.getId())
                .setName(user.getName())
                .setCode(user.getCode())
                .setPassword(user.getPassword())
                .addAllRoles(user.getRoles().stream().map(role -> {
                  return tech.lmru.auth.grpc.service.generated.impl.Role.newBuilder()
                      .setId(role.getId())
                      .setName(role.getName())
                      .setCode(role.getCode())
                      .addAllPermissions(role.getPermissions().stream().map(permission -> {
                        return tech.lmru.auth.grpc.service.generated.impl.Permission.newBuilder()
                            .setId(permission.getId())
                            .setCode(permission.getCode())
                            .setName(permission.getName())
                            .build();
                      }).collect(Collectors.toList()))
                      .build();
                }).collect(Collectors.toList())).build();
          }).collect(Collectors.toList());
      userAllResponse = UserAllResponse.newBuilder().addAllUsers(collect).build();
    }
    responseObserver.onNext(userAllResponse);
    responseObserver.onCompleted();

  }


  @Override
  public void readByIdUser(EntityIdRequest request, StreamObserver<User> responseObserver) {
    Integer id = Integer.valueOf(request.getId());
    Optional<tech.lmru.entity.User> byId = userRepository.findById(id);

    if (byId.isPresent()) {
      tech.lmru.entity.User user = byId.get();
      Set<Role> roles = user.getRoles();
      Collection<tech.lmru.auth.grpc.service.generated.impl.Role> rolesCollect = roles.stream()
          .map(role -> {
            return tech.lmru.auth.grpc.service.generated.impl.Role.newBuilder()
                .setId(role.getId())
                .setCode(role.getCode())
                .setName(role.getName())
                .addAllPermissions(role.getPermissions().stream().map(permission -> {
                  return tech.lmru.auth.grpc.service.generated.impl.Permission.newBuilder()
                      .setId(permission.getId())
                      .setCode(permission.getCode())
                      .setName(permission.getName())
                      .build();
                }).collect(Collectors.toList()))
                .build();
          }).collect(Collectors.toList());

      User userImpl = User.newBuilder()
          .setId(user.getId())
          .setCode(user.getCode())
          .setName(user.getName())
          .setPassword(user.getPassword())
          .addAllRoles(rolesCollect)
          .build();

      responseObserver.onNext(userImpl);
      responseObserver.onCompleted();
    } else {
      responseObserver.onCompleted();
      throw new EntityNotFoundException();
    }

  }

  @Override
  public void deleteByIdUser(EntityIdRequest request,
      StreamObserver<EntityDeleteResponse> responseObserver) {
    Integer id = Integer.valueOf(request.getId());
    EntityDeleteResponse entityDeleteResponse = null;
    try {
      userRepository.deleteById(id);
      entityDeleteResponse = EntityDeleteResponse.newBuilder().setSuccess(true).build();
    } catch (Exception e) {
      entityDeleteResponse = EntityDeleteResponse.newBuilder().setSuccess(false).build();
    } finally {
      responseObserver.onNext(entityDeleteResponse);
      responseObserver.onCompleted();
    }
  }

}
