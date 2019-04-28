package pl.prv.techalive.encoder.methods;

import pl.prv.techalive.core.Encryptable;
import pl.prv.techalive.encoder.Base64;
import pl.prv.techalive.encoder.Encoder;
import pl.prv.techalive.encoder.constants.Constants;
import pl.prv.techalive.encoder.constants.Padding;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES Encrypt/Decrypt class
 */
public class DES extends BaseMethod{

    private static final String DES_CFB = "DES/CFB";
    private static final String DES_OFB = "DES/OFB";

    private static final int KEY_LEGHT = 8;
    private static final int VECTOR_LEGHT = 8;

    /**
     * All supported methods
     */
    public enum Method implements Encryptable {
        DES_ECB_NoPadding ("DES/ECB/NoPadding"),
        DES_ECB_PKCS5Padding ("DES/ECB/PKCS5Padding"),
        // DES_ECB_PKCS7Padding ("DES/ECB/PKCS7Padding"), //Cannot find any provider supporting
        DES_ECB_ISO10126Padding ("DES/ECB/ISO10126Padding"),

        DES_CBC_NoPadding ("DES/CBC/NoPadding"),
        DES_CBC_PKCS5Padding ("DES/CBC/PKCS5Padding"),
        // DES_CBC_PKCS7Padding ("DES/CBC/PKCS7Padding"), //Cannot find any provider supporting
        DES_CBC_ISO10126Padding ("DES/CBC/ISO10126Padding"),

        DES_CTR_NoPadding ("DES/CTR/NoPadding"),
        DES_CTR_PKCS5Padding ("DES/CTR/PKCS5Padding"),
        // DES_CTR_PKCS7Padding ("DES/CTR/PKCS7Padding"), //Cannot find any provider supporting
        // DES_CTR_ISO10126Padding ("DES/CTR/ISO10126Padding"), // No installed provider

        DES_CTS_NoPadding ("DES/CTS/NoPadding"),
        DES_CTS_PKCS5Padding ("DES/CTS/PKCS5Padding"),
        // DES_CTS_PKCS7Padding ("DES/CTS/PKCS7Padding"), //Cannot find any provider supporting
        // DES_CTS_ISO10126Padding ("DES/CTS/ISO10126Padding"), // No installed provider

        DES_CFB_NoPadding ("DES/CFB/NoPadding"),
        DES_CFB_PKCS5Padding ("DES/CFB/PKCS5Padding"),
        // DES_CFB_PKCS7Padding ("DES/CFB/PKCS7Padding"), //Cannot find any provider supporting
        DES_CFB_ISO10126Padding ("DES/CFB/ISO10126Padding"),

        DES_OFB_NoPadding ("DES/OFB/NoPadding"),
        DES_OFB_PKCS5Padding ("DES/OFB/PKCS5Padding"),
        // DES_OFB_PKCS7Padding ("DES/OFB/PKCS7Padding"), // Cannot find any provider
        DES_OFB_ISO10126Padding ("DES/OFB/ISO10126Padding");

        private final String method;

        Method(String method) {
            this.method = method;
        }

        public String getMethod() {
            return method;
        }


        // Multiples of 8 to work with DES
        @Override
        public int getLengthMultiple() {
            return 8;
        }

        @Override
        public String encrypt(String text) {
            return Encoder.BuilderDES()
                    .message(text)
                    .method(this)
                    .encrypt();
        }

        @Override
        public String decrypt(String text) {
            return Encoder.BuilderDES().message(text)
                    .method(this)
                    .decrypt();
        }
    }

    static abstract class MethodMode{

        private MethodMode(String method){
            this.method = method;
        }

        static String method;

        static boolean checkNumber(int methodNumber){
            if(methodNumber >= 8 && methodNumber <= 64){
                return true;
            } else {
                throw new IllegalStateException(Constants.METHOD_CFB_OFB_EXCEPTION);
            }
        }

        public String getMethod() {
            return method;
        }
    }

    /**
     * DES-CBF encryption methods
     * This class implements setting of encryption method number
     */
    public static class MethodCFB extends MethodMode {

        private MethodCFB(String method) {
            super(method);
        }

        public static MethodCFB generateMethod(int methodNumber, Padding padding){
            if(checkNumber(methodNumber)){
                return new MethodCFB(DES_CFB + methodNumber + "/" + padding.getPadding());
            } else {
                return null;
            }
        }
    }

    /**
     * DES-OBF encryption methods
     * This class implements setting of encryption method number
     */
    public static class MethodOFB extends MethodMode {

        private MethodOFB(String method) {
            super(method);
        }

        public static MethodOFB generateMethod(int methodNumber, Padding padding){
            if(checkNumber(methodNumber)){
                return new MethodOFB(DES_OFB + methodNumber + "/" + padding.getPadding());
            } else {
                return null;
            }
        }
    }

    /**
     * Implementation of DES encryption
     */
    public static String encrypt(String method, byte[] key, byte[] vector, byte[] message) throws Exception {

//        generate Key
        byte[] keyBytes = generateKey(key, KEY_LEGHT);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "DES");

//        generate Initialization Vector
        byte[] keyBytesIv = generateVector(vector, VECTOR_LEGHT);
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytesIv);

        Cipher cipher = Cipher.getInstance(method);

        if(hasInitVector(method)){
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        }

        byte[] cipherText = cipher.doFinal(message);

        return Base64.encodeToString(cipherText, Base64.DEFAULT);
    }

    /**
     * Implementation of DES decryption
     */
    public static String decrypt(String method, byte[] key, byte[] vector, byte[] message) throws Exception{

//        generate Key
        byte[] keyBytes = generateKey(key, KEY_LEGHT);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "DES");

//        generate Initialization Vector
        byte[] keyBytesIv = generateVector(vector, VECTOR_LEGHT);
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytesIv);

        Cipher cipher = Cipher.getInstance(method);

        if(hasInitVector(method)){
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
        }

        byte[] cipherText = cipher.doFinal(Base64.decode(message, Base64.DEFAULT));

        return new String(cipherText);
    }
}
