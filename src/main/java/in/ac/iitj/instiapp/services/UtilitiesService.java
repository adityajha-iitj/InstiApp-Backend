package in.ac.iitj.instiapp.services;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UtilitiesService {


    private final SecureRandom secureRandom;

    public UtilitiesService(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    public String generateRandom(String intialString) {
        return String.format("%s-%d%d", intialString, System.currentTimeMillis(), secureRandom.nextInt(1000));
    }

    public String generateRandomString(int length){
        return RandomStringUtils.random(length, 0, 0, true, true, null, secureRandom);

    }
}
