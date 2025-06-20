package in.ac.iitj.instiapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
    public class OAuthTokenExchangeException extends RuntimeException {
        public OAuthTokenExchangeException(String message) {
            super(message);
        }
}
