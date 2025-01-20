package site.iotify.userservice.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {


    private Long id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String email;

    private String username;

    private String password;

    private String auth;

    private String provider;
}
