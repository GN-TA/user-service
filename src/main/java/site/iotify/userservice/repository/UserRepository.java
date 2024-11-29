package site.iotify.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.iotify.userservice.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
