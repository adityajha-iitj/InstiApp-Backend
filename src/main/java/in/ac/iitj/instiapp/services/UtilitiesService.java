package in.ac.iitj.instiapp.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;

@Service
public class UtilitiesService {


    private final SecureRandom secureRandom;
    private final ObjectMapper objectMapper;

    @Autowired
    public UtilitiesService(SecureRandom secureRandom, ObjectMapper objectMapper) {
        this.secureRandom = secureRandom;
        this.objectMapper = objectMapper;
    }

    public String generateRandom(String intialString) {
        return String.format("%s-%d%d", intialString, System.currentTimeMillis(), secureRandom.nextInt(1000));
    }

    public String generateRandomString(int length){
        return RandomStringUtils.random(length, 0, 0, true, true, null, secureRandom);

    }

    public void writeToResponse(HttpServletResponse response, Object message, HttpStatus status) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }


}
