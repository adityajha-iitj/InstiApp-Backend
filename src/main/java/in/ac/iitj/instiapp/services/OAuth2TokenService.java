package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.Repository.OAuth2TokenRepository;
import in.ac.iitj.instiapp.Repository.impl.OAuthTokenRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class OAuth2TokenService {


   private  final OAuth2TokenRepository oAuthTokenRepository;


   public OAuth2TokenService(OAuth2TokenRepository oAuthTokenRepository) {
       this.oAuthTokenRepository = oAuthTokenRepository;
   }







}
