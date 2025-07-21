package in.ac.iitj.instiapp.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource; // Import Spring's Resource class
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FCMIntializer {

    private static final Logger logger = LoggerFactory.getLogger(FCMIntializer.class);

    // --- FIX 1: Inject a Resource object, not just a String path ---
    @Value("${app.firebase-configuration-file}")
    private Resource firebaseConfigFile;

    @PostConstruct
    public void initialize() {
        try {
            // --- FIX 2: Get the InputStream directly from the Resource object ---
            // This single line works for both "classpath:" and "file:" paths.
            InputStream serviceAccountStream = firebaseConfigFile.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application has been initialized successfully.");
            }
        } catch (IOException e) {
            logger.error("Error initializing Firebase. Could not load resource: " + firebaseConfigFile, e);
            throw new RuntimeException("Could not initialize Firebase.", e);
        }
    }
}