package in.ac.iitj.instiapp.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FCMIntializer {

    private static final Logger logger = LoggerFactory.getLogger(FCMIntializer.class);

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            // Make sure the file is in src/main/resources/
            ClassPathResource resource = new ClassPathResource(firebaseConfigPath);

            if (!resource.exists()) {
                // Throw an exception with a very clear message
                throw new IOException("Firebase config file not found at path: " + firebaseConfigPath);
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application has been initialized successfully.");
            }
        } catch (IOException e) {
            // Log the error and re-throw it to stop the application startup
            logger.error("Error initializing Firebase.", e);
            throw new RuntimeException("Could not initialize Firebase.", e);
        }
    }
}