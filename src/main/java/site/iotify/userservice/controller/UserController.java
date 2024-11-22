package site.iotify.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import site.iotify.userservice.dto.UserDto;
import site.iotify.userservice.exception.UserAlreadyExistsException;
import site.iotify.userservice.exception.UserNotFoundException;
import site.iotify.userservice.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
    @GetMapping("/user")
    public ResponseEntity<UserDto> fetchUserById(String id) {
        UserDto userDto = userService.loadUserById(id);
        if (userDto == null) {
            throw new UserNotFoundException(id);
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
     *         사용자 중복 시 HTTP 409(CONFLICT) 상태와 오류 메시지를 반환
     * @throws UserAlreadyExistsException 사용자가 이미 존재할 경우 발생
     */
    @PostMapping("/user")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userRegistrationRequest) {
        try {
            userService.registerUser(userRegistrationRequest);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userRegistrationRequest.getId());
    }
}
