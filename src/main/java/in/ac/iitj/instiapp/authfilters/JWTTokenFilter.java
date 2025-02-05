package in.ac.iitj.instiapp.authfilters;

import in.ac.iitj.instiapp.services.JWTTokens.JWTApprovedTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenFilter {

    private final JWTApprovedTokenService jwtApprovedTokenService;


    @Autowired
    public JWTTokenFilter(JWTApprovedTokenService jwtApprovedTokenService) {
        this.jwtApprovedTokenService = jwtApprovedTokenService;
    }







}
