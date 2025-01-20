package site.iotify.userservice.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.iotify.userservice.domain.user.service.EmailVerificationService;
import java.util.Map;

@RestController
@Slf4j
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/email-verify")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody Map<String, String> email) {
        log.info("Email verification request received: {}", email);
        emailVerificationService.generateVerificationCode(email.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        log.info("Verification code check request received: email={}, code={}", email, code);
        boolean isVerified = emailVerificationService.verifyCode(email, code);
        if (isVerified) {
            log.info("Email verification success");
            return ResponseEntity.ok().build();
        } else {
            log.warn("Email verification failed");
            return ResponseEntity.badRequest().body("Invalid verification code");
        }
    }
}
