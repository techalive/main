package pl.prv.techalive.encoder.builders;

import pl.prv.techalive.encoder.constants.Constants;
import pl.prv.techalive.encoder.methods.HMAC;

/**
 * HMAC Encrypt/Decrypt Builder
 */
public class BuilderHMAC extends BaseBuilder<BuilderHMAC>{

    private volatile HMAC.Method method;

    private volatile byte[] key;

    /**
     * Set the encryption method for encrypting or decrypting
     */
    public BuilderHMAC method(HMAC.Method method) {
        this.method = method;
        return this;
    }

    /**
     * Set the encryption key
     */
    public BuilderHMAC key(String key) {
        this.key = key.getBytes();
        return this;
    }

    public BuilderHMAC key(byte[] key) {
        this.key = key;
        return this;
    }

    /**
     * HMAC encryption method doesn`t support decryption
     */
    @Deprecated
    public String decrypt() {
        return super.decrypt();
    }

    @Deprecated
    public void decryptAsync() {
        return;
    }

    @Override
    String encryption() throws Exception {
        return HMAC.encrypt(method, key, message);
    }

    @Deprecated
    String decryption() throws Exception {
        throw new NoSuchMethodError();
    }

    @Override
    boolean hasEnoughData() {
        if(method == null){
            throw new NullPointerException(Constants.METHOD_EXCEPTION);
        }
        if(message == null){
            throw new NullPointerException(Constants.MESSAGE_EXCEPTION);
        }
        return true;
    }
}
