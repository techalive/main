package pl.prv.techalive.encoder.methods;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import pl.prv.techalive.encoder.Base64;

/**
 * DESede Encrypt/Decrypt class
 */
public class DESede extends BaseMethod{

    private static final int VECTOR_LEGHT = 8;

    /**
     * All supported methods
     */
    public enum Method{
        DESEDE ("DESEDE"),
        DESEDE_CBC_NoPadding ("DESEDE/CBC/NoPadding"),
        DESEDE_CBC_PKCS5Padding ("DESEDE/CBC/PKCS5Padding"),
        DESEDE_CBC_PKCS7Padding ("DESEDE/CBC/PKCS7Padding"),
        DESEDE_CBC_ISO10126Padding ("DESEDE/CBC/ISO10126Padding");

        private final String method;

        Method(String method) {
            this.method = method;
        }

        public String getMethod() {
            return method;
        }
    }

    /**
     * Keysize must be equal to 128 or 192 bits.
     * Default Keysize equals 128 bits.
     */
    public enum Key{
        SIZE_128 (16),
        SIZE_192 (24);

        private final int size;

        Key(int size) {
            this.size = size;
        }
    }

    /**
     * Implementation of DESede encryption
     */
    public static String encrypt(Method method, byte[] key, Key keySize, byte[] vector, byte[] message) throws Exception{

//        generate Key
        byte[] keyBytes = generateKey(key, keySize.size);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, method.getMethod());

//        generate Initialization Vector
        byte[] keyBytesIv = generateVector(vector, VECTOR_LEGHT);
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytesIv);

        Cipher cipher = Cipher.getInstance(method.getMethod());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(message);

        return Base64.encodeToString(cipherText, Base64.DEFAULT);
    }

    /**
     * Implementation of DESede decryption
     */
    public static String decrypt(Method method, byte[] key, Key keySize, byte[] vector, byte[] message) throws Exception{

//        generate Key
        byte[] keyBytes = generateKey(key, keySize.size);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, method.getMethod());

//        generate Initialization Vector
        byte[] keyBytesIv = generateVector(vector, VECTOR_LEGHT);
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytesIv);

        Cipher cipher = Cipher.getInstance(method.getMethod());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(Base64.decode(message, Base64.DEFAULT));

        return new String(cipherText);
    }
}
