package in.ac.iitj.instiapp.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FCMIntializer {

    private static final Logger logger = LoggerFactory.getLogger(FCMIntializer.class);

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath; // This will be "/app/config/firebase_key.json"

    @PostConstruct
    public void initialize() {
        try {
            // --- THIS IS THE FIX ---
            // We now use FileInputStream to read from a direct filesystem path,
            // not ClassPathResource which only checks the classpath.
            InputStream serviceAccountStream = new FileInputStream(firebaseConfigPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application has been initialized successfully.");
            }
        } catch (IOException e) {
            // This will now correctly report an error if the volume mount fails.
            logger.error("Error initializing Firebase. Could not find file at filesystem path: " + firebaseConfigPath, e);
            throw new RuntimeException("Could not initialize Firebase.", e);
        }
    }
}