package site.iotify.userservice.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Id
    private String id;

    private String username;

    private String password;

    private String auth;

    private String provider;
}
