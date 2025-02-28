package in.ac.iitj.instiapp.Utils;

import in.ac.iitj.instiapp.services.JWTTokens.JWEConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

public class CookieHelper {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "RefreshToken";

    public static final String HEADER_SAMESITE_STRICT = "Strict";
    public static final String HEADER_SAMESITE_NONE = "None";
    public static final String HEADER_SAMESITE_LAX = "Lax";

    public static void  setAuthCookie(HttpServletResponse response, String cookieValue, JWEConstants.ExpirationDuration duration,String sameSiteHeader ) {
        Cookie cookie = new Cookie("Authorization", String.format("Bearer%s",cookieValue));
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (duration.getDuration()/1000));
        cookie.setAttribute("SameSite", sameSiteHeader);
        response.addCookie(cookie);
    }

    public static Optional<String> getAuthCookieValue(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, "Authorization");
        if (cookie != null) {
            return Optional.of(cookie.getValue().substring(6));
        }
        return Optional.empty();
    }

    public static void deleteAuthCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("Authorization", "");
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    public static void setRefreshTokenCookie(HttpServletResponse response, String cookieValue,
                                             String sameSiteHeader) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, cookieValue);
        cookie.setPath("/api/v1/auth/refresh-token"); // Restrict scope
        cookie.setSecure(true); // Send only over HTTPS
        cookie.setHttpOnly(true); // Prevent JavaScript access
        cookie.setMaxAge((int) (JWEConstants.ExpirationDuration.LONG.getDuration() / 1000)); // Convert milliseconds to seconds
        cookie.setAttribute("SameSite", sameSiteHeader);
        response.addCookie(cookie);
    }

    public static Optional<String> getRefreshTokenCookieValue(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, REFRESH_TOKEN_COOKIE_NAME);
        return Optional.ofNullable(cookie).map(Cookie::getValue);
    }

    public static void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, "");
        cookie.setPath("/api/v1/auth/refresh-token"); // Ensure same path as creation
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Expire immediately
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    public static void deleteJSessionIdCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setPath("/"); // Adjust if your JSESSIONID cookie uses a different path
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Instructs the browser to delete the cookie immediately
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

}
