package site.iotify.userservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import site.iotify.userservice.domain.EmailVerificationToken;
import site.iotify.userservice.repository.EmailVerificationTokenRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    public EmailVerificationService(JavaMailSender mailSender, EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.mailSender = mailSender;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
    }

    public void generateToken(String email) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken emailToken = new EmailVerificationToken(token, email, LocalDateTime.now().plusHours(24));
        emailVerificationTokenRepository.save(token, emailToken);

        log.info("Email verification token generated: {}", token);
        log.info("email: {}", email);
        sendVerificationEmail(email, token);
    }

    public boolean verifyToken(String token) {
        EmailVerificationToken emailToken = emailVerificationTokenRepository.findByToken(token);

        if (emailToken == null || emailToken.isExpired()) {
            return false;
        }
        emailVerificationTokenRepository.delete(token);
        return true;
    }

    public boolean isEmailVerified(String email) {

        EmailVerificationToken emailToken = emailVerificationTokenRepository.findByEmail(email);
        if (emailToken == null || emailToken.isExpired()) {
            return false;
        }
        return true;
    }

    private void sendVerificationEmail(String email, String token) {
        String link = "http://localhost:8090/verify-email?token=" + token;
        try {
            ClassPathResource resource = new ClassPathResource("./verificationEmail.html");
            String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            htmlContent = htmlContent.replace("{{verificationLink}}", link);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(email);
            helper.setFrom("noreply@iotify.site");
            helper.setSubject("IoTify 회원가입 인증코드");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
