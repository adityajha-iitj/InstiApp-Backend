package in.ac.iitj.instiapp.database.entities.Auth;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @Column(nullable = true)
    String refreshToken;



    public OAuth2Tokens(String username, String deviceId, String accessToken, String refreshToken ) {
        this.Id = null;
        this.user = new User(username);
        this.deviceId = deviceId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

    }
}
