package site.iotify.userservice.domain.tenant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import site.iotify.userservice.domain.user.entity.User;

import java.io.Serializable;

import lombok.*;

@Entity
@Table(name = "tenant_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TenantUser {

    @EmbeddedId
    private PK pk;

    @ManyToOne
    @MapsId("userId") // PK의 userId와 매핑
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("tenantId") // PK의 tenantId와 매핑
    @JoinColumn(name = "tenant_id", insertable = false, updatable = false)
    private Tenant tenant;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class PK implements Serializable {
        @Column(name = "user_id")
        private String userId;

        @Column(name = "tenant_id")
        private String tenantId;
    }
}

