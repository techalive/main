package pl.prv.techalive.encoder.methods;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * HMAC Encrypt/Decrypt class
 */
public class HMAC {

    /**
     * All supported methods
     */
    public enum Method{

        HMAC_MD5("HMAC-MD5"),
        HMAC_SHA_1("HMAC-SHA1"),
        HMAC_SHA_224("HMAC-SHA224"),
        HMAC_SHA_256("HMAC-SHA256"),
        HMAC_SHA_384("HMAC-SHA384"),
        HMAC_SHA_512("HMAC-SHA512");


        private final String method;

        Method(String method) {
            this.method = method;
        }

        public String getMethod() {
            return method;
        }

    }

    /**
     * Implementation of HMAC encryption
     */
    public static String encrypt(HMAC.Method method, byte[] key, byte[] message) throws Exception{

        SecretKeySpec keySpec = new SecretKeySpec(key, method.getMethod());

        Mac cipher = Mac.getInstance(method.getMethod());
        cipher.init(keySpec);
        byte[] cipherText = cipher.doFinal(message);

        StringBuffer hash = new StringBuffer();
        for (int i = 0; i < cipherText.length; i++) {
            String hex = Integer.toHexString(0xFF & cipherText[i]);
            if (hex.length() == 1) {
                hash.append('0');
            }
            hash.append(hex);
        }

        return hash.toString();
    }

}
