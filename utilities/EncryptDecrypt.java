package gr.deddie.pfr.utilities;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptDecrypt {

    private static final String SECRET_KEY_1 = "z%C*F-JaNdRgUkXp";

    private SecretKeySpec secretKeySpec;
    private Cipher cipher;

    public EncryptDecrypt() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
        secretKeySpec = new SecretKeySpec(SECRET_KEY_1.getBytes(StandardCharsets.UTF_8), "AES");
        cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
    }


    public String encrypt(String toBeEncrypt) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(toBeEncrypt.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encrypted) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
