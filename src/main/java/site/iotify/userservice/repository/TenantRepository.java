package site.iotify.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.iotify.userservice.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {
}
