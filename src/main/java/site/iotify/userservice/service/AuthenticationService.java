package site.iotify.userservice.service;

import org.springframework.stereotype.Service;
import site.iotify.userservice.UserRepository;
import site.iotify.userservice.entity.User;
import site.iotify.userservice.exception.UserNotFoundException;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 주어진 사용자 ID를 기반으로 해당 사용자의 인코딩된 비밀번호를 조회합니다.
     *
     * <p>이 메서드는 {@code userRepository}를 사용하여 지정된 ID에 해당하는 사용자를 검색합니다.
     * 사용자가 존재하지 않을 경우 {@code UserNotFoundException}을 발생시킵니다.
     * 사용자가 존재하면 해당 사용자의 인코딩된 비밀번호를 반환합니다.
     *
     * @param id 조회할 사용자의 고유 ID. null이거나 비어 있어서는 안 됩니다.
     * @return 지정된 ID에 해당하는 사용자의 인코딩된 비밀번호.
     * @throws UserNotFoundException 지정된 {@code id}에 해당하는 사용자가 없을 경우 발생합니다.
     */
    public String loadPassword(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user.getPassword();
    }
}
