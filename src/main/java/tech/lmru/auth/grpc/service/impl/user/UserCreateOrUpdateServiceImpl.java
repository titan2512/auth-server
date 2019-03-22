package tech.lmru.auth.grpc.service.impl.user;

import io.grpc.stub.StreamObserver;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityCreateResponse;
import tech.lmru.auth.grpc.service.generated.impl.User;
import tech.lmru.auth.grpc.service.generated.impl.UserCreateOrUpdateServiceGrpc;
import tech.lmru.entity.Permission;
import tech.lmru.entity.Role;
import tech.lmru.repo.UserRepository;

@GRPCService
public class UserCreateOrUpdateServiceImpl extends
    UserCreateOrUpdateServiceGrpc.UserCreateOrUpdateServiceImplBase {

  private UserRepository userRepository;

  @Autowired
  public UserCreateOrUpdateServiceImpl(UserRepository userRepository) {
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
}
