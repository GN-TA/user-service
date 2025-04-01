package site.iotify.userservice.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import site.iotify.userservice.domain.user.dto.request.UserRequestDto;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Id
    private String id;
    private String username;
    private String password;
    private String auth;
    private String provider;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_admin")
    private boolean isAdmin;
    private String note;

    public void updateUserInfo(UserRequestDto.UserUpdate userDto) {
        this.email = userDto.getEmail();
        this.username = userDto.getUsername();
        this.auth = userDto.getAuth();
        this.isActive = userDto.isActive();
        this.note = userDto.getNote();
    }

    public void updatePassword(UserRequestDto.UserPasswordChange userDto) {
        this.password = userDto.getNewPassword();
    }
}
