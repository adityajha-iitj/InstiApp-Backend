package in.ac.iitj.instiapp.Tests.InitialiseEntities.Auth;

import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Tests.InitialiseEntities.User.InitialiseUser;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.Auth.JWTRefreshToken;
import in.ac.iitj.instiapp.database.entities.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt.JwtRefreshTokenTestData.*;
import in.ac.iitj.instiapp.Repository.JWTRefreshTokenRepository;

import static in.ac.iitj.instiapp.Tests.EntityTestData.AuthAndJwt.JwtRefreshTokenTestData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;

@Component
@Import({InitialiseUser.class})
public class InitialiseJWTRefreshToken implements InitialiseEntities.Initialise {

    private final UserRepository userRepository;
    private final JWTRefreshTokenRepository tokenRepository;

    @Autowired
    public InitialiseJWTRefreshToken(InitialiseUser initialiseUser, UserRepository userRepository, JWTRefreshTokenRepository tokenRepository) {
        initialiseUser.initialise();
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void initialise() {
        JWTRefreshToken refreshToken1 = JWT_REFRESH_TOKEN_1.toEntity();
        JWTRefreshToken refreshToken2 = JWT_REFRESH_TOKEN_2.toEntity();
        JWTRefreshToken refreshToken3 = JWT_REFRESH_TOKEN_3.toEntity();

        refreshToken1.setUser(new User(userRepository.usernameExists(USER1.userName)));
        refreshToken2.setUser(new User(userRepository.usernameExists(USER2.userName)));
        refreshToken3.setUser(new User(userRepository.usernameExists(USER3.userName)));

        tokenRepository.save(refreshToken1);
        tokenRepository.save(refreshToken2);
        tokenRepository.save(refreshToken3);

    }
}
