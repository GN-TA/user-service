package site.iotify.userservice.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.iotify.userservice.domain.user.service.AuthenticationService;

@Controller
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 주어진 사용자 ID에 대한 인코딩된 비밀번호를 조회하는 요청을 처리합니다.
     *
     * <p>이 엔드포인트는 제공된 사용자 ID를 기반으로 해당 사용자의
     * 인코딩된 비밀번호를 조회합니다. 조회 작업은 {@code authenticationService}에 위임되며,
     * 조회된 비밀번호를 응답으로 반환합니다.
     *
     * <p><b>사용 방법:</b> {@code /pwd} 엔드포인트로 GET 요청을 보내고, 필수 쿼리 파라미터 {@code id}를 포함해야 합니다.
     *
     * @param id 조회할 사용자의 고유 ID. 이 파라미터는 필수이며 null 또는 비어 있을 수 없습니다.
     * @return 인코딩된 비밀번호를 {@link String} 형태로 담은 {@link ResponseEntity} 객체.
     *         HTTP 상태 코드는 {@code 200 (OK)}입니다.
     * @throws IllegalArgumentException 제공된 {@code id}가 null이거나 비어 있을 경우 발생합니다.
     */
    @GetMapping("/pwd")
    public ResponseEntity<String> fetchPassword(@RequestParam String id) {
        return ResponseEntity.ok(authenticationService.loadPassword(id));
    }
}
