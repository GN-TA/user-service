package site.iotify.userservice.domain.tenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.iotify.userservice.domain.tenant.entity.TenantUser;

import java.util.List;

public interface TenantUserRepository extends JpaRepository<TenantUser, TenantUser.PK> {
    List<TenantUser> findByPkUserId(String userId);
}
