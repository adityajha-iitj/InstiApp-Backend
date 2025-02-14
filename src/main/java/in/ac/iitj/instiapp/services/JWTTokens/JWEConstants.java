package in.ac.iitj.instiapp.services.JWTTokens;

public class JWEConstants {


    public static final String KEYS_DEVICE_ID = "deviceId";
    public static final String KEYS_OAUTH2_ACCESS_TOKEN = "oauth2AccessToken";
    public static final String KEYS_OAUTH2_REFRESH_TOKEN = "oauth2RefreshToken";
    public static final String KEYS_STATE = "state";
    public static final String KEYS_EMAIL = "email";
    public static final String KEYS_NAME = "name";
    public static final String KEYS_AVATAR = "avatar";
    public static final String KEYS_PHONE_NUMBER = "phoneNumber";
    public static final String KEYS_USER_TYPE = "userType";


    public enum STATES {
        STATE_UNREGISTERED,  // Only redirection URL is sent
        STATE_PENDING,       // OAuth2 tokens generated but phone numbers not verified
        STATE_VERIFIED,      // Phone numbers are verified but profile is incomplete
        STATE_APPROVED;      // Profile is complete
    }


    // Predefined expiration durations
    public enum ExpirationDuration {
        /**
         * 5 minutes <br>
         * Can be used during oauth2 redirection happens
         */
        VERY_SHORT(300_000L),
        /**
         * 10 minutes <br>
         * Can be used after oauth2 for signup
         */
        SHORT(600_000L),
        /**
         * 15 minutes <br>
         * For access JWE
         */
        MEDIUM(900_000L),
        /**
         * 6 months <br>
         * For refresh Token
         */
        LONG(15_552_000_000L);

        private final long duration;

        ExpirationDuration(long duration) {
            this.duration = duration;
        }

        public long getDuration() {
            return duration;
        }
    }
}
