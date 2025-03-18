package site.iotify.userservice.domain.user.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import site.iotify.userservice.domain.tenant.entity.TenantUser;
import site.iotify.userservice.domain.tenant.repository.TenantUserRepository;
import site.iotify.userservice.domain.user.entity.User;
import site.iotify.userservice.domain.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class TestData {
    private final UserRepository userRepository;
    private final TenantUserRepository tenantUserRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void loadTestData() {
        User user = new User("test@test.com", "e4cb2550-d93c-4627-968d-fdfd113da28d", "test",
                passwordEncoder.encode("123"), "ADMIN", "");
        userRepository.save(user);
        System.out.println("Test users added successfully!");
    }
}
