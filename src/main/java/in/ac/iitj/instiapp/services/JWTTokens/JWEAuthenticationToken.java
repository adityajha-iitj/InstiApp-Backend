package in.ac.iitj.instiapp.services.JWTTokens;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class JWEAuthenticationToken extends AbstractAuthenticationToken {


    @Getter
    private final JWEConstants.STATES state;
    private final String subject;
    private final JWTClaimsSet claimsSet;



    public JWEAuthenticationToken(JWEConstants.STATES state, String subject,JWTClaimsSet claimsSet) {
        super(Collections.emptyList());

        this.state = state;
        this.subject = subject;
        this.claimsSet = claimsSet;
        

        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return claimsSet;
    }

    @Override
    public Object getPrincipal() {
        return subject;
    }

}
