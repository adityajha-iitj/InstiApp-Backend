package in.ac.iitj.instiapp.services.JWTTokens;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public class JWEAuthenticationToken extends AbstractAuthenticationToken {


    @Getter
    private final JWEConstants.STATES state;
    private final String subject;
    private final JWTClaimsSet claimsSet;



    public JWEAuthenticationToken(JWEConstants.STATES state, String subject, JWTClaimsSet claimsSet) {
        super(List.of(new SimpleGrantedAuthority("ROLE_USER")));
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

    @Override
    public String getName() {
        return subject;
    }

}
