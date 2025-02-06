package in.ac.iitj.instiapp.Tests.InitialiseEntities.Auth;

import in.ac.iitj.instiapp.Repository.JWTRefreshTokenRepository;
import in.ac.iitj.instiapp.Repository.OAuth2TokenRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseUser;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.Auth.JWTRefreshToken;
import in.ac.iitj.instiapp.database.entities.Auth.OAuth2Tokens;
import in.ac.iitj.instiapp.database.entities.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt.JwtRefreshTokenTestData.*;
import in.ac.iitj.instiapp.Repository.OAuth2TokenRepository;

import static in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt.JwtRefreshTokenTestData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt.OAuth2TokensTestData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;

@Component
@Import({InitialiseUser.class})
public class InitialiseOAuth2Token implements InitialiseEntities.Initialise {

    private final UserRepository userRepository;
    private final OAuth2TokenRepository tokenRepository;

    @Autowired
    public InitialiseOAuth2Token(InitialiseUser initialiseUser, UserRepository userRepository, OAuth2TokenRepository tokenRepository) {
        initialiseUser.initialise();
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void initialise() {
        OAuth2Tokens oAuth2Token1 = OAUTH2_TOKEN_1.toEntity();
        OAuth2Tokens oAuth2Token2 = OAUTH2_TOKEN_2.toEntity();
        OAuth2Tokens oAuth2Token3 = OAUTH2_TOKEN_3.toEntity();

        oAuth2Token1.setUser(new User(userRepository.usernameExists(USER1.userName)));
        oAuth2Token2.setUser(new User(userRepository.usernameExists(USER2.userName)));
        oAuth2Token3.setUser(new User(userRepository.usernameExists(USER3.userName)));

        tokenRepository.save(oAuth2Token1);
        tokenRepository.save(oAuth2Token2);
        tokenRepository.save(oAuth2Token3);

    }
}
