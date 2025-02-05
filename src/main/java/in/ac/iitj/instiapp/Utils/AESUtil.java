package in.ac.iitj.instiapp.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESUtil {


    private static final String AES_Algorithm = "AES";
    private static final Logger log = LoggerFactory.getLogger(AESUtil.class);

    private static Cipher cipherEncryptor;
    private static Cipher cipherDecryptor;


    private static void refreshKeyAndCipher(){
        try {
            SecretKey secretKey = KeyGenerator.getInstance(AES_Algorithm).generateKey();
            cipherEncryptor = Cipher.getInstance(AES_Algorithm);
            cipherEncryptor.init(Cipher.ENCRYPT_MODE, secretKey);
            cipherDecryptor = Cipher.getInstance(AES_Algorithm);
            cipherDecryptor.init(Cipher.DECRYPT_MODE, secretKey);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException  | InvalidKeyException e){
            log.error(e.getMessage());
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    static {
      refreshKeyAndCipher();
    }


    public static String  encrypt(String payload){
        try {
            return Base64.getEncoder().encodeToString( cipherEncryptor.doFinal(payload.getBytes()));
        } catch (IllegalBlockSizeException  | BadPaddingException e) {
            log.error(e.getMessage());
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static String decrypt(String payload){
        try {
            return new String(cipherDecryptor.doFinal(Base64.getDecoder().decode(payload)));
        }catch (IllegalBlockSizeException | BadPaddingException e){
            log.error(e.getMessage());
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void reloadKey(){
        refreshKeyAndCipher();
    }

}
