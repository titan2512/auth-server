package tech.lmru.auth.grpc.service.impl.role;

import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.Empty;
import tech.lmru.auth.grpc.service.generated.impl.Permission;
import tech.lmru.auth.grpc.service.generated.impl.RoleAllResponse;
import tech.lmru.auth.grpc.service.generated.impl.RoleReadAllServiceGrpc;
import tech.lmru.entity.Role;
import tech.lmru.repo.RoleRepository;

@GRPCService
public class RoleReadAllServiceImpl extends RoleReadAllServiceGrpc.RoleReadAllServiceImplBase {

  private RoleRepository roleRepository;

  @Autowired
  public RoleReadAllServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public void readAllRole(Empty request, StreamObserver<RoleAllResponse> responseObserver) {
    List<Role> all = roleRepository.findAll();
    RoleAllResponse response = null;
    if (all.isEmpty()){
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
              Permission permission1 = Permission.newBuilder()
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
}
