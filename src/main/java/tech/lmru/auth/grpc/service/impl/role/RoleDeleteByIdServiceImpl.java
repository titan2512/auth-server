package tech.lmru.auth.grpc.service.impl.role;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityDeleteResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.RoleDeleteByIdServiceGrpc;
import tech.lmru.repo.RoleRepository;

@GRPCService
public class RoleDeleteByIdServiceImpl extends
    RoleDeleteByIdServiceGrpc.RoleDeleteByIdServiceImplBase {

  private RoleRepository roleRepository;

  @Autowired
  public RoleDeleteByIdServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
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
