package tech.lmru.auth.grpc.service.impl.role;

import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityCreateResponse;
import tech.lmru.auth.grpc.service.generated.impl.Role;
import tech.lmru.auth.grpc.service.generated.impl.RoleCreateOrUpdateServiceGrpc;
import tech.lmru.entity.Permission;
import tech.lmru.repo.RoleRepository;

@GRPCService
public class RoleCreateOrUpdateServiceImpl extends
    RoleCreateOrUpdateServiceGrpc.RoleCreateOrUpdateServiceImplBase {

  private RoleRepository roleRepository;

  @Autowired
  public RoleCreateOrUpdateServiceImpl(RoleRepository roleRepository) {
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
}
