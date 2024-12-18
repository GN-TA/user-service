package site.iotify.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "tenant_user")
@NoArgsConstructor
public class TenantUser {
    @EmbeddedId
    private PK pk;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JoinColumn(name = "tenant_id")
    @ManyToOne
    private Tenant tenant;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PK implements Serializable {
        @Column(name = "user_id")
        private String userId;
        @Column(name = "tenant_id")
        private String tenantId;
    }
}
