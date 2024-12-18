package site.iotify.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tenants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private boolean hasGateway;
    private int gatewayMaxCount;
    private int deviceMaxCount;
    private String ip;
}
