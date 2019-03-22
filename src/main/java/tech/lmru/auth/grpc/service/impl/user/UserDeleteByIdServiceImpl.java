package tech.lmru.auth.grpc.service.impl.user;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import tech.lmru.auth.grpc.config.GRPCService;
import tech.lmru.auth.grpc.service.generated.impl.EntityDeleteResponse;
import tech.lmru.auth.grpc.service.generated.impl.EntityIdRequest;
import tech.lmru.auth.grpc.service.generated.impl.UserDeleteByIdServiceGrpc;
import tech.lmru.repo.UserRepository;

@GRPCService
public class UserDeleteByIdServiceImpl extends
    UserDeleteByIdServiceGrpc.UserDeleteByIdServiceImplBase {

  private UserRepository userRepository;

  @Autowired
  public UserDeleteByIdServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
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
