package site.iotify.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.iotify.userservice.dto.ChangePasswordRequest;
import site.iotify.userservice.dto.UserDto;
import site.iotify.userservice.exception.UnAuthenticatedException;
import site.iotify.userservice.exception.UnAuthorizedException;
import site.iotify.userservice.exception.UserAlreadyExistsException;
import site.iotify.userservice.exception.UserNotFoundException;
import site.iotify.userservice.service.EmailVerificationService;
import site.iotify.userservice.service.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    public UserController(UserService userService, EmailVerificationService emailVerificationService) {
        this.userService = userService;
        this.emailVerificationService = emailVerificationService;
    }

    /**
     * 주어진 ID를 가진 사용자를 데이터베이스에서 조회합니다.
     *
     * <p>이 메서드는 사용자 고유 식별자를 기반으로 사용자의 정보를 조회하는 GET 요청을 처리합니다.
     * 사용자가 존재하지 않을 경우 {@link UserNotFoundException} 예외를 발생시킵니다.</p>
     *
     * @param id 조회할 사용자의 고유 식별자
     * @return 사용자의 정보를 담은 {@link UserDto}를 포함한 {@link ResponseEntity}
     * @throws UserNotFoundException 주어진 ID를 가진 사용자가 존재하지 않을 경우 발생
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> fetchUserById(@PathVariable String id) {
        UserDto userDto = userService.loadUserById(id);
        if (userDto == null) {
            throw new UserNotFoundException(id);
        }
        return ResponseEntity.ok(userDto);
    }

    /**
     * 주어진 email을 가진 사용자를 데이터베이스에서 조회합니다.
     *
     * <p>이 메서드는 사용자 이메일 아이디를 기반으로 사용자의 정보를 조회하는 GET 요청을 처리합니다.
     * 사용자가 존재하지 않을 경우 {@link UserNotFoundException} 예외를 발생시킵니다.</p>
     *
     * @param email 조회할 사용자의 고유 식별자
     * @return 사용자의 정보를 담은 {@link UserDto}를 포함한 {@link ResponseEntity}
     * @throws UserNotFoundException 주어진 이메일을 가진 사용자가 존재하지 않을 경우 발생
     */
    @GetMapping("/user")
    public ResponseEntity<UserDto> fetchUserByEmail(@RequestParam String email) {
        UserDto userDto = userService.loadUserByEmail(email);
        if (userDto == null) {
            throw new UserNotFoundException(email);
        }
        return ResponseEntity.ok(userDto);
    }

    /**
     * 새로운 사용자를 등록합니다.
     *
     * <p>이 메서드는 사용자 등록 요청을 처리하며, 요청 본문에 포함된 사용자 정보를 기반으로
     * 새로운 사용자를 데이터베이스에 저장합니다. 사용자가 이미 존재할 경우
     * {@link UserAlreadyExistsException} 예외를 처리하여 HTTP 409(CONFLICT) 상태 코드를 반환합니다.</p>
     *
     * @param userRegistrationRequest 등록할 사용자 정보를 담은 {@link UserDto}
     * @return 성공 시 등록된 사용자의 ID를 포함한 HTTP 201(CREATED) 응답,
     * 사용자 중복 시 HTTP 409(CONFLICT) 상태와 오류 메시지를 반환
     * @throws UserAlreadyExistsException 사용자가 이미 존재할 경우 발생
     */
    @PostMapping("/user")
    @CrossOrigin(origins = "http://localhost:8080")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userRegistrationRequest) {
        if (userRegistrationRequest.getProvider() == null &&
                !emailVerificationService.isEmailVerified(userRegistrationRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증이 필요합니다.");
        }
        try {
            userService.registerUser(userRegistrationRequest);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 사용자의 비밀번호 변경 요청을 처리합니다.
     *
     * <p>이 메서드는 제공된 비밀번호 변경 요청 데이터를 검증합니다. 검증 내용은 다음과 같습니다:
     * <ul>
     *   <li>새 비밀번호와 확인 비밀번호가 일치하는지 확인</li>
     *   <li>새 비밀번호가 기존 비밀번호와 동일하지 않은지 확인</li>
     *   <li>제공된 이메일에 해당하는 사용자가 존재하는지 확인</li>
     * </ul>
     * 모든 검증이 통과되면 {@code userService}를 통해 사용자의 비밀번호를 업데이트합니다.
     *
     * @param changePasswordRequest 사용자의 이메일, 기존 비밀번호, 새 비밀번호, 확인 비밀번호를 포함한 비밀번호 변경 요청 데이터
     * @return 비밀번호 변경 성공 시 HTTP 상태 코드 200(OK)을 포함하는 {@code ResponseEntity}
     * @throws IllegalArgumentException 다음 경우에 발생:
     *                                  <ul>
     *                                    <li>새 비밀번호와 확인 비밀번호가 일치하지 않을 경우</li>
     *                                    <li>새 비밀번호가 기존 비밀번호와 동일할 경우</li>
     *                                  </ul>
     * @throws UserNotFoundException    제공된 이메일에 해당하는 사용자가 존재하지 않을 경우 발생
     */
    @PostMapping("/pwd")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new IllegalArgumentException("New password cannot be the same");
        }
        if (userService.loadUserByEmail(changePasswordRequest.getEmail()) == null) {
            throw new UserNotFoundException(changePasswordRequest.getEmail());
        }
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 사용자의 정보를 업데이트합니다.
     *
     * <p>이 메서드는 전달된 사용자 정보를 기반으로 해당 사용자의 정보를 업데이트합니다.
     * 제공된 이메일을 통해 사용자를 조회한 후, 사용자가 존재하지 않으면 {@code UserNotFoundException}을 발생시킵니다.
     * 사용자가 존재할 경우, {@code userService}를 통해 사용자 정보를 업데이트합니다.
     *
     * @param userDto 업데이트할 사용자 정보를 포함한 데이터 객체
     * @return 사용자 정보 업데이트 성공 시 HTTP 상태 코드 200(OK)을 포함하는 {@code ResponseEntity}
     * @throws UserNotFoundException 제공된 이메일에 해당하는 사용자가 존재하지 않을 경우 발생
     */
    @PostMapping("/user-info")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        UserDto user = userService.loadUserByEmail(userDto.getEmail());
        if (user == null) {
            throw new UserNotFoundException(userDto.getEmail());
        }
        userService.updateUserInfo(userDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 사용자의 권한을 변경합니다.
     *
     * <p>이 메서드는 요청 사용자의 정보를 기반으로 권한 변경 작업을 수행합니다.
     * 요청자가 관리자 권한(ROLE_ADMIN)을 가지고 있는 경우, 대상 사용자의 권한을 업데이트합니다.
     * 요청 사용자가 인증되지 않았거나 관리자 권한이 없는 경우, 각각 예외를 발생시킵니다.
     *
     * @param requestUserDto 권한 변경 요청을 수행하는 사용자 정보를 포함한 데이터 객체
     * @param targetEmail    권한을 변경할 대상 사용자의 이메일 주소
     * @param auth           변경할 권한 값
     * @return 권한 변경 성공 시 HTTP 상태 코드 200(OK)을 포함하는 {@code ResponseEntity}
     * @throws UnAuthorizedException    요청 사용자가 인증되지 않은 경우 발생
     * @throws UnAuthenticatedException 요청 사용자가 관리자 권한이 없는 경우 발생
     */
    @PostMapping("/auth")
    public ResponseEntity<String> changeAuth(@RequestBody UserDto requestUserDto,
                                             @RequestParam("targetEmail") String targetEmail,
                                             @RequestParam("auth") String auth) {
        UserDto requestUser = userService.loadUserByEmail(requestUserDto.getEmail());
        if (requestUser == null) {
            throw new UnAuthorizedException();
        }
        if (!requestUser.getAuth().equals("ROLE_ADMIN")) {
            throw new UnAuthenticatedException();
        }
        userService.changeAuth(requestUserDto, targetEmail, auth);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 사용자를 삭제합니다.
     *
     * <p>이 메서드는 전달된 사용자 정보를 기반으로 해당 사용자를 삭제합니다.
     * 제공된 이메일을 통해 사용자를 조회한 후, 사용자가 존재하지 않을 경우 {@code UserNotFoundException}을 발생시킵니다.
     * 사용자가 존재할 경우, {@code userService}를 통해 사용자를 삭제합니다.
     *
     * @param userDto 삭제할 사용자 정보를 포함한 데이터 객체
     * @return 사용자 삭제 성공 시 HTTP 상태 코드 200(OK)을 포함하는 {@code ResponseEntity}
     * @throws UserNotFoundException 제공된 이메일에 해당하는 사용자가 존재하지 않을 경우 발생
     */
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestBody UserDto userDto) {
        if (userService.loadUserByEmail(userDto.getEmail()) == null) {
            throw new UserNotFoundException(userDto.getEmail());
        }
        userService.deleteUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
