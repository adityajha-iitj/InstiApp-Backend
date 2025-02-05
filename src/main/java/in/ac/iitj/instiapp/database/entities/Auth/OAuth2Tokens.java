package in.ac.iitj.instiapp.database.entities.Auth;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OAuth2Tokens {

    @jakarta.persistence.Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long Id;


    @ManyToOne(optional = false)
    User user;

    @Column(nullable = false)
    String deviceId;

    @Column(nullable = false)
    String accessToken;

    @Column(nullable = false)
    String refreshToken;


    @Column(nullable = false)
    Timestamp refreshTokenExpiresAt;


    public OAuth2Tokens(String username, String deviceId, String accessToken, String refreshToken, Timestamp refreshTokenExpiresAt) {
        this.Id = null;
        this.user = new User(username);
        this.deviceId = deviceId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }
}
