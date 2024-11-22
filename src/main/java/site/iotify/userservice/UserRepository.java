package site.iotify.userservice;

import org.springframework.data.jpa.repository.JpaRepository;
import site.iotify.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}
