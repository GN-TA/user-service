package site.iotify.userservice.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.iotify.userservice.domain.user.dto.ChirpstackUserInfo;
import site.iotify.userservice.domain.user.dto.request.ChirpstackCreateUserRequestDto;
import site.iotify.userservice.domain.user.repository.UserRepository;
import site.iotify.userservice.domain.user.dto.ChangePasswordRequest;
import site.iotify.userservice.domain.user.dto.UserDto;
import site.iotify.userservice.domain.user.entity.User;
import site.iotify.userservice.global.adaptor.ChirpstackAdaptor;
import site.iotify.userservice.global.exception.UnAuthenticatedException;
import site.iotify.userservice.global.exception.UnAuthorizedException;
import site.iotify.userservice.global.exception.UserAlreadyExistsException;
import site.iotify.userservice.global.exception.UserNotFoundException;

@Service
public class UserService {

    private final ChirpstackAdaptor chirpstackAdaptor;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ChirpstackAdaptor chirpstackAdaptor) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.chirpstackAdaptor = chirpstackAdaptor;
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
     * 주어진 email을 사용하여 데이터베이스에서 사용자를 조회합니다.
     *
     * <p>해당 email을 가진 사용자가 데이터베이스에 존재하면 {@link UserDto}로 변환하여 반환하며,
     * 존재하지 않을 경우 {@code null}을 반환합니다.</p>
     *
     * @param email 조회할 사용자의 이메일 아이디
     * @return {@link UserDto} 객체 또는 사용자가 존재하지 않을 경우 {@code null}
     */
    public UserDto loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
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
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        if (user != null) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        ChirpstackUserInfo chirpstackUserInfo = ChirpstackUserInfo.builder()
                .id(userDto.getEmail())
                .email(userDto.getEmail())
                .isAdmin(false)
                .isActive(true)
                .build();

        String chirpstackUserId = chirpstackAdaptor.addUsersInNetwork(null,
                new ChirpstackCreateUserRequestDto(
                        chirpstackUserInfo,
                        userDto.getPassword(),
                        null
                )
        );
        userDto.setId(chirpstackUserId);

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        userDto.setAuth("ROLE_USER");
        User newUser = userDto.toEntity();
        userRepository.save(newUser);
    }

    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * <p>이 메서드는 사용자의 이메일을 기반으로 사용자를 조회하고,
     * 기존 비밀번호가 일치하는지 확인한 후 새 비밀번호로 업데이트합니다.
     * 비밀번호는 안전하게 해싱한 후 저장됩니다.
     *
     * <p>비밀번호 변경 절차는 다음과 같습니다:
     * <ol>
     *   <li>이메일을 기반으로 사용자를 조회</li>
     *   <li>기존 비밀번호와 저장된 비밀번호를 비교</li>
     *   <li>새 비밀번호를 암호화한 후 사용자 정보 업데이트</li>
     * </ol>
     *
     * @param changePasswordRequest 비밀번호 변경에 필요한 사용자 이메일, 기존 비밀번호, 새 비밀번호를 포함한 요청 객체
     * @throws UserNotFoundException    제공된 이메일에 해당하는 사용자가 존재하지 않을 경우 발생
     * @throws UnAuthenticatedException 기존 비밀번호가 올바르지 않을 경우 발생
     */
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByEmail(changePasswordRequest.getEmail()).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(changePasswordRequest.getEmail());
        }
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new UnAuthenticatedException();
        }
        String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        UserDto updatedUser = new UserDto().fromEntity(user);
        updatedUser.setPassword(encodedPassword);
        userRepository.save(updatedUser.toEntity());
    }

    /**
     * 사용자의 이름을 업데이트합니다.
     *
     * <p>이 메서드는 사용자 이메일을 기반으로 해당 사용자를 조회한 후, 사용자의 다른 정보가 변경되지 않았는지 확인한 다음,
     * 사용자 이름만 업데이트합니다. 다른 정보(비밀번호, ID, 권한, Provider 등)는 이 API를 통해 변경할 수 없습니다.
     *
     * <p>업데이트 절차는 다음과 같습니다:
     * <ol>
     *   <li>이메일을 기반으로 사용자를 조회</li>
     *   <li>사용자가 이미 존재하는 경우 예외 발생</li>
     *   <li>사용자의 비밀번호, ID, 권한, Provider가 일치하지 않으면 예외 발생</li>
     *   <li>사용자 이름을 업데이트한 후 저장</li>
     * </ol>
     *
     * @param userDto 업데이트할 사용자 정보를 포함한 데이터 객체
     * @throws UserNotFoundException    제공된 이메일에 해당하는 사용자가 존재하지 않을 경우 발생
     * @throws IllegalArgumentException 유저 이름 이외의 다른 정보를 수정하려 할 경우 발생
     */
    public void updateUserInfo(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(user.getEmail());
        }
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword()) ||
                !userDto.getId().equals(user.getId()) ||
                !userDto.getAuth().equals(user.getAuth()) ||
                !userDto.getProvider().equals(user.getProvider())
        ) {
            throw new IllegalArgumentException("해당 API를 통해서는 유저이름만 수정 가능");
        }

        UserDto updatedUser = new UserDto().fromEntity(user);
        updatedUser.setUsername(userDto.getUsername());
        userRepository.save(updatedUser.toEntity());
    }

    /**
     * 대상 사용자의 권한을 변경합니다.
     *
     * <p>이 메서드는 요청 사용자가 관리자 권한(ROLE_ADMIN)을 가지고 있는 경우,
     * 대상 사용자의 권한을 새로 지정합니다. 요청자의 인증 정보와 권한을 검증하며,
     * 권한이 없는 경우 예외를 발생시킵니다.
     *
     * <p>권한 변경 절차는 다음과 같습니다:
     * <ol>
     *   <li>요청 사용자를 이메일로 조회</li>
     *   <li>요청 사용자의 비밀번호와 권한 검증</li>
     *   <li>대상 사용자를 이메일로 조회</li>
     *   <li>대상 사용자의 권한을 업데이트</li>
     * </ol>
     *
     * @param requestUserDto 권한 변경 요청을 수행하는 사용자 정보를 포함한 데이터 객체
     * @param targetEmail    권한을 변경할 대상 사용자의 이메일
     * @param auth           새로 설정할 권한 값
     * @throws UserNotFoundException    요청 사용자가 존재하지 않을 경우 발생
     * @throws UnAuthorizedException    요청 사용자의 비밀번호가 일치하지 않을 경우 발생
     * @throws UnAuthenticatedException 요청 사용자가 관리자 권한(ROLE_ADMIN)이 아닐 경우 발생
     */
    public void changeAuth(UserDto requestUserDto, String targetEmail, String auth) {
        User requestUser = userRepository.findByEmail(requestUserDto.getEmail()).orElse(null);
        if (requestUser == null) {
            throw new UserNotFoundException(requestUserDto.getEmail());
        }
        if (!passwordEncoder.matches(requestUserDto.getPassword(), requestUser.getPassword())) {
            throw new UnAuthorizedException();
        }
        if (!requestUser.getAuth().equals("ROLE_ADMIN")) {
            throw new UnAuthenticatedException();
        }
        User targetUser = userRepository.findByEmail(targetEmail).orElse(null);
        UserDto targetUserDto = new UserDto().fromEntity(targetUser);
        targetUserDto.setAuth(auth);
        userRepository.save(targetUserDto.toEntity());
    }

    /**
     * 사용자를 삭제합니다.
     *
     * <p>이 메서드는 전달된 사용자 정보를 기반으로 사용자를 삭제합니다.
     * 사용자를 이메일로 조회한 후, 해당 사용자가 존재하지 않거나 비밀번호가 일치하지 않으면 예외를 발생시킵니다.
     *
     * <p>삭제 절차는 다음과 같습니다:
     * <ol>
     *   <li>이메일을 기반으로 사용자 조회</li>
     *   <li>사용자가 존재하지 않으면 {@code UserNotFoundException} 발생</li>
     *   <li>비밀번호가 일치하지 않으면 {@code UnAuthorizedException} 발생</li>
     *   <li>사용자를 삭제</li>
     * </ol>
     *
     * @param userDto 삭제할 사용자 정보를 포함한 데이터 객체
     * @throws UserNotFoundException 제공된 이메일에 해당하는 사용자가 존재하지 않을 경우 발생
     * @throws UnAuthorizedException 제공된 비밀번호가 저장된 비밀번호와 일치하지 않을 경우 발생
     */
    public void deleteUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(userDto.getEmail());
        }
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new UnAuthorizedException();
        }
        userRepository.delete(user);
    }
}
