package tech.lmru.auth.grpc.service.impl.user;

import io.grpc.stub.StreamObserver;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.Permission;
import tech.lmru.auth.grpc.service.generated.impl.User;
import tech.lmru.auth.grpc.service.generated.impl.UserReadByIdServiceGrpc;
import tech.lmru.entity.Role;
import tech.lmru.repo.UserRepository;

@GRPCService
public class UserReadByIdServiceImpl extends UserReadByIdServiceGrpc.UserReadByIdServiceImplBase {

  private UserRepository userRepository;

  @Autowired
  public UserReadByIdServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void readByIdUser(EntityIdRequest request, StreamObserver<User> responseObserver) {
    Integer id = Integer.valueOf(request.getId());
    Optional<tech.lmru.entity.User> byId = userRepository.findById(id);

    if (byId.isPresent()){
      tech.lmru.entity.User user = byId.get();
      Set<Role> roles = user.getRoles();
      Collection<tech.lmru.auth.grpc.service.generated.impl.Role> rolesCollect = roles.stream().map(role -> {
         return tech.lmru.auth.grpc.service.generated.impl.Role.newBuilder()
            .setId(role.getId())
            .setCode(role.getCode())
            .setName(role.getName())
            .addAllPermissions(role.getPermissions().stream().map(permission -> {
              return Permission.newBuilder()
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
}
