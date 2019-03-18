package tech.lmru.auth.grpc.service.impl.role;

import io.grpc.stub.StreamObserver;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.Role;
import tech.lmru.auth.grpc.service.generated.impl.RoleReadByIdServiceGrpc;
import tech.lmru.entity.Permission;
import tech.lmru.repo.RoleRepository;

@GRPCService
public class RoleReadByIdServiceImpl extends RoleReadByIdServiceGrpc.RoleReadByIdServiceImplBase {

  private RoleRepository roleRepository;


  @Autowired
  public RoleReadByIdServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
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
}
