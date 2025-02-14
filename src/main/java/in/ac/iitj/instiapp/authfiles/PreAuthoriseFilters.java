package in.ac.iitj.instiapp.authfiles;


import in.ac.iitj.instiapp.services.JWTTokens.JWEAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

@Service("preAuthorizeFilters")
public class PreAuthoriseFilters {


    public boolean hasState(String expectedState, Authentication authentication){
        System.out.println(expectedState);
        if(authentication instanceof JWEAuthenticationToken jweAuthenticationToken){
            System.out.println(jweAuthenticationToken.getState().name());
            return Objects.equals(expectedState, jweAuthenticationToken.getState().name());
        }
        return  false;
    }


    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("@preAuthorizeFilters.hasState('STATE_PENDING', authentication)")
    public @interface HasPendingState{}

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("@preAuthorizeFilters.hasState('STATE_APPROVED', authentication)")
    public @interface HasApprovedState{}
}
