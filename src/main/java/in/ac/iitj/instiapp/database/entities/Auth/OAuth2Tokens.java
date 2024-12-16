package in.ac.iitj.instiapp.database.entities.Auth;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class OAuth2Tokens {

    @jakarta.persistence.Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long Id;


    @ManyToOne(optional = false)
    User user;

    @Column(nullable = false)
    String DeviceId;

    @Column(nullable = false)
    String AccessToken;

    @Column(nullable = false)
    String RefreshToken;


    @Column(nullable = false)
    Timestamp RefreshTokenExpiresAt;
}
