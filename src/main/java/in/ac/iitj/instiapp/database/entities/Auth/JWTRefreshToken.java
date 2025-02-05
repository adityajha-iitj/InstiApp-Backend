package in.ac.iitj.instiapp.database.entities.Auth;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "jwt_refresh_token")
@Getter
@Setter
public class JWTRefreshToken {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @ManyToOne(optional = false)
    User user;

    @Column(unique = true, nullable = false)
    String deviceId;

    @Column(nullable = false)
    String refreshToken;

    Instant refreshTokenExpires;
}
