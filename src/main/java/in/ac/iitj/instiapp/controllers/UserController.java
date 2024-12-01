package in.ac.iitj.instiapp.controllers;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserController {

     @GetMapping("/")
    public String test(){
        return  "Hello World";
    }

    @GetMapping("/secured")
    public Map<String , Object> test2(@AuthenticationPrincipal OAuth2User principal){
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }
}
