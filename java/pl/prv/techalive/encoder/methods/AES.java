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
 * AES Encrypt/Decrypt class
 */
public class AES extends BaseMethod {

    private static final String AES_CFB = "AES/CFB";
    private static final String AES_OFB = "AES/OFB";

    private static final int VECTOR_LEGHT = 16;

    /**
     * All supported methods
     */
    public enum Method implements Encryptable {

//        AES("AES"),

        AES_CBC_NO_PADDING("AES/CBC/NoPadding"),
        AES_CBC_PKCS5PADDING("AES/CBC/PKCS5Padding"),
        //AES_CBC_PKCS7PADDING("AES/CBC/PKCS7Padding"),
        AES_CBC_ISO10126PADDING("AES/CBC/ISO10126Padding"),

        AES_CTR_NO_PADDING("AES/CTR/NoPadding"),
        AES_CTR_PKCS5PADDING("AES/CTR/PKCS5Padding"),

        // AES_CTR_PKCS7PADDING("AES/CTR/PKCS7Padding"), // doesnt work
        // AES_CTR_ISO10126PADDING("AES/CTR/ISO10126Padding"), // doesnt work

        AES_CFB_NO_PADDING("AES/CFB/NoPadding"),
        AES_CFB_PKCS5PADDING("AES/CFB/PKCS5Padding"),
        //        AES_CFB_PKCS7PADDING ("AES/CFB/PKCS7Padding"), //Cannot find any provider supporting AES/CFB/PKCS7Padding
        AES_CFB_ISO10126PADDING("AES/CFB/ISO10126Padding"),

//        AES_CFB1_NO_PADDING ("AES/CFB1/NoPadding"), // it has a bug
//        AES_CFB1_PKCS5PADDING ("AES/CFB1/PKCS5Padding"), // it has a bug
//        AES_CFB1_PKCS7PADDING ("AES/CFB1/PKCS7Padding"), // it has a bug
//        AES_CFB1_ISO10126PADDING ("AES/CFB1/ISO10126Padding"), // it has a bug

        AES_ECB_NO_PADDING("AES/ECB/NoPadding"),
        AES_ECB_PKCS5PADDING("AES/ECB/PKCS5Padding"),
        //        AES_ECB_PKCS7PADDING ("AES/ECB/PKCS7Padding"), //Cannot find any provider supporting AES/ECB/PKCS7Padding
        AES_ECB_ISO10126PADDING("AES/ECB/ISO10126Padding"),

//        AES_GCM_NO_PADDING ("AES/GCM/NoPadding"), //Unsupported parameter: javax.crypto.spec.IvParameterSpec@4520ebad

        AES_OFB_NO_PADDING("AES/OFB/NoPadding"),
        AES_OFB_PKCS5PADDING("AES/OFB/PKCS5Padding"),
        //        AES_OFB_PKCS7PADDING ("AES/OFB/PKCS7Padding"), // Cannot find any provider supporting AES/OFB/PKCS7Padding
        AES_OFB_ISO10126PADDING("AES/OFB/ISO10126Padding");

        private final String method;

        Method(String method) {
            this.method = method;
        }

        public String getMethod() {
            return method;
        }

        public String encrypt(String text) {
            return Encoder.BuilderAES()
                    .message(text)
                    .method(this)
                    .keySize(AES.Key.SIZE_128)
                    .encrypt();
        }

        @Override
        public String decrypt(String text) {
            return Encoder.BuilderAES().message(text)
                    .method(this)
                    .keySize(AES.Key.SIZE_128)
                    .decrypt();
        }

        // Multiples of 16 to work with DES
        @Override
        public int getLengthMultiple() {
            return 16;
        }
    }

    static abstract class MethodMode {

        private MethodMode(String method) {
            this.method = method;
        }

        static String method;

        static boolean checkNumber(int methodNumber) {
            if (methodNumber >= 8 && methodNumber <= 128) {
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
     * AES-CBF encryption methods
     * This class implements setting of encryption method number
     */
    public static class MethodCFB extends MethodMode {

        private MethodCFB(String method) {
            super(method);
        }

        public static MethodCFB generateMethod(int methodNumber, Padding padding) {
            if (checkNumber(methodNumber)) {
                return new MethodCFB(AES_CFB + methodNumber + "/" + padding.getPadding());
            } else {
                return null;
            }
        }
    }

    /**
     * AES-OBF encryption methods
     * This class implements setting of encryption method number
     */
    public static class MethodOFB extends MethodMode {

        private MethodOFB(String method) {
            super(method);
        }

        public static MethodOFB generateMethod(int methodNumber, Padding padding) {
            if (checkNumber(methodNumber)) {
                return new MethodOFB(AES_OFB + methodNumber + "/" + padding.getPadding());
            } else {
                return null;
            }
        }
    }

    /**
     * Keysize must be equal to 128, 192, or 256 bits.
     * Default Keysize equals 128 bits.
     */
    public enum Key {
        SIZE_128(16),
        SIZE_192(24),
        SIZE_256(32);

        private final int type;

        Key(int type) {
            this.type = type;
        }
    }

    /**
     * Implementation of AES encryption
     */
    public static String encrypt(String method, byte[] key, Key keyType, byte[] vector, byte[] message) throws Exception {

//        generate Key
        byte[] keyBytes = generateKey(key, keyType.type);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

//        generate Initialization Vector
        byte[] keyBytesIv = generateVector(vector, VECTOR_LEGHT);
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytesIv);

        Cipher cipher = Cipher.getInstance(method);

        if (hasInitVector(method)) {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        }

        byte[] cipherText = cipher.doFinal(message);

        return Base64.encodeToString(cipherText, Base64.DEFAULT);
    }

    /**
     * Implementation of AES decryption
     */
    public static String decrypt(String method, byte[] key, Key keyType, byte[] vector, byte[] message) throws Exception {

//        generate Key
        byte[] keyBytes = generateKey(key, keyType.type);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

//        generate Initialization Vector
        byte[] keyBytesIv = generateVector(vector, VECTOR_LEGHT);
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytesIv);

        Cipher cipher = Cipher.getInstance(method);

        if (hasInitVector(method)) {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
        }

        byte[] cipherText = cipher.doFinal(Base64.decode(message, Base64.DEFAULT));

        return new String(cipherText);
    }
}
