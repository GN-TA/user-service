package site.iotify.userservice.domain.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();


    public EmailVerificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean isEmailVerified(String email) {
        String emailToken = verificationCodes.get(email);
        if (emailToken == null) {
            return false;
        }
        return true;
    }

    private void sendVerificationEmail(String email, String token) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(email);
            helper.setSubject("이메일 인증 코드");
            helper.setText(buildEmailContent(token), true);

            mailSender.send(mimeMessage);
            log.info("Email sent to {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildEmailContent(String code) {
        return "<h1>이메일 인증 코드</h1>" +
                "<p>아래 코드를 회원가입 화면에 입력하세요:</p>" +
                "<h2 style='color: blue;'>" + code + "</h2>";
    }

    public String generateVerificationCode(String email) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        verificationCodes.put(email, code);
        sendVerificationEmail(email, code);
        return code;
    }

    public boolean verifyCode(String email, String code) {
        return code.equals(verificationCodes.get(email));
    }

}
