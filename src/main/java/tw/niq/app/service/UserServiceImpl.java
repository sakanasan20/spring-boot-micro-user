package tw.niq.app.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import tw.niq.app.dto.UserDto;
import tw.niq.app.entity.UserEntity;
import tw.niq.app.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	private final  UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		
		userDetails.setUserId(UUID.randomUUID().toString());
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
		userEntity.setEncryptedPassword("test");
		
		UserEntity createdUserEntity = userRepository.save(userEntity);
		
		UserDto returnValue = modelMapper.map(createdUserEntity, UserDto.class);
		
		return returnValue;
	}

}
