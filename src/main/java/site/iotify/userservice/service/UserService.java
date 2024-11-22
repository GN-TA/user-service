package site.iotify.userservice.service;

import org.springframework.stereotype.Service;
import site.iotify.userservice.UserRepository;
import site.iotify.userservice.dto.UserDto;
import site.iotify.userservice.entity.User;
import site.iotify.userservice.exception.UserAlreadyExistsException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 주어진 ID를 사용하여 데이터베이스에서 사용자를 조회합니다.
     *
     * <p>해당 ID를 가진 사용자가 데이터베이스에 존재하면 {@link UserDto}로 변환하여 반환하며,
     * 존재하지 않을 경우 {@code null}을 반환합니다.</p>
     *
     * @param id 조회할 사용자의 고유 식별자
     * @return {@link UserDto} 객체 또는 사용자가 존재하지 않을 경우 {@code null}
     */
    public UserDto loadUserById(String id) {
        User user = userRepository.findById(id).orElse(null);
        return new UserDto().fromEntity(user);
    }

    /**
     * 새로운 사용자를 등록합니다.
     *
     * <p>요청된 사용자 ID가 이미 데이터베이스에 존재할 경우
     * {@link UserAlreadyExistsException} 예외를 발생시킵니다.
     * 존재하지 않는 경우 요청된 데이터를 기반으로 새 사용자를 생성하고 저장합니다.</p>
     *
     * @param userDto 등록할 사용자 정보를 담은 {@link UserDto}
     * @throws UserAlreadyExistsException 주어진 ID를 가진 사용자가 이미 존재할 경우 발생
     */
    public void registerUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElse(null);
        if (user != null) {
            throw new UserAlreadyExistsException(user.getId());
        }
        User newUser = userDto.toEntity();
        userRepository.save(newUser);
    }
}
