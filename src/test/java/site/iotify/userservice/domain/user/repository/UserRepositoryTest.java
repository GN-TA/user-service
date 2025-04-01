package site.iotify.userservice.domain.user.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import site.iotify.userservice.domain.user.entity.User;

import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByEmail 테스트")
    void findByEmail() {
        User user = new User(
                "test@gmail.com",
                "codethestudent",
                "junyeong",
                "q1w2e3r4",
                "ADMIN",
                null,
                true,
                false,
                "this is simple bio "
        );

        entityManager.persistAndFlush(user); // 저장 후 즉시 플러시
        Assertions.assertTrue(userRepository.findByEmail("test@gmail.com").isPresent());

        entityManager.clear();

        Optional<User> foundUser = userRepository.findByEmail("test@gmail.com");

        Assertions.assertFalse(entityManager.getEntityManager().contains(user));

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertTrue(foundUser.get().getUsername().equals("junyeong"));
    }
}