package tw.niq.app.repository;

import org.springframework.data.repository.CrudRepository;

import tw.niq.app.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);
	
}
