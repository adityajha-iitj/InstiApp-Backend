package in.ac.iitj.instiapp.services;


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
}
