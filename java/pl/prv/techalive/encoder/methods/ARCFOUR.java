package pl.prv.techalive.encoder.methods;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import pl.prv.techalive.encoder.Base64;

/**
 * ARCFOUR Encrypt/Decrypt class
 */
public class ARCFOUR extends BaseMethod{

    /**
     * ARCFOUR method
     */
    private static final String ARCFOUR = "ARCFOUR";

    /**
     * Implementation of ARCFOUR encryption
     */
    public static String encrypt(byte[] key, int keySize, byte[] message) throws Exception {

//        generate Key
        byte[] keyBytes = generateKey(key, keySize);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ARCFOUR);

        Cipher cipher = Cipher.getInstance(ARCFOUR);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] cipherText = cipher.doFinal(message);

        return Base64.encodeToString(cipherText, Base64.DEFAULT);
    }

    /**
     * Implementation of ARCFOUR decryption
     */
    public static String decrypt(byte[] key, int keySize, byte[] message) throws Exception {

//        generate Key
        byte[] keyBytes = generateKey(key, keySize);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ARCFOUR);

        Cipher cipher = Cipher.getInstance(ARCFOUR);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] cipherText = cipher.doFinal(Base64.decode(message, Base64.DEFAULT));

        return new String(cipherText);
    }
}
