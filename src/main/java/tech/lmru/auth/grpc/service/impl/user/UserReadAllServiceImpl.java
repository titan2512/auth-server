package tech.lmru.auth.grpc.service.impl.user;

import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.Empty;
import tech.lmru.auth.grpc.service.generated.impl.Permission;
import tech.lmru.auth.grpc.service.generated.impl.Role;
import tech.lmru.auth.grpc.service.generated.impl.UserAllResponse;
import tech.lmru.auth.grpc.service.generated.impl.UserReadAllServiceGrpc;
import tech.lmru.entity.User;
import tech.lmru.repo.UserRepository;

@GRPCService
public class UserReadAllServiceImpl extends UserReadAllServiceGrpc.UserReadAllServiceImplBase {

  private UserRepository userRepository;

  @Autowired
  public UserReadAllServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void readAllUser(Empty request, StreamObserver<UserAllResponse> responseObserver) {

    List<User> allUsers = userRepository.findAll();
    UserAllResponse userAllResponse = null;
    if (allUsers.isEmpty()){
      userAllResponse = UserAllResponse.newBuilder().build();
    } else {
      List<tech.lmru.auth.grpc.service.generated.impl.User> collect = allUsers.stream().map(user -> {
        return tech.lmru.auth.grpc.service.generated.impl.User.newBuilder()
            .setId(user.getId())
            .setName(user.getName())
            .setCode(user.getCode())
            .setPassword(user.getPassword())
            .addAllRoles(user.getRoles().stream().map(role -> {
              return Role.newBuilder()
                  .setId(role.getId())
                  .setName(role.getName())
                  .setCode(role.getCode())
                  .addAllPermissions(role.getPermissions().stream().map(permission -> {
                    return Permission.newBuilder()
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
}
