package com.gestionnegocios.api_gestionnegocios.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {

    @Value("${encryption.secret.key}")
    private String secret;

    private SecretKeySpec secretKey;
    private static final String ALGORITHM = "AES";

    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(secret.getBytes(), ALGORITHM);
    }

    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar", e);
        }
    }

    public boolean matches(String encryptedDataFrontend, String encryptedDataDB) {
        try {
            return encryptedDataFrontend.equals(encryptedDataDB);
        } catch (Exception e) {
            throw new RuntimeException("Error al comparar", e);
        }
    }
}
