package site.iotify.userservice.domain.tenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.iotify.userservice.domain.tenant.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {
}
