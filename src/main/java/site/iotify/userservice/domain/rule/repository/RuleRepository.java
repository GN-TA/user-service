package site.iotify.userservice.domain.rule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.iotify.userservice.domain.rule.entity.Rule;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Integer> {
    List<Rule> findAllByTenantId(String tenantId);
}
