package pl.prv.techalive.encoder.builders;

import pl.prv.techalive.encoder.constants.Constants;
import pl.prv.techalive.encoder.methods.ARCFOUR;

/**
 * ARCFOUR Encrypt/Decrypt Builder
 */
public class BuilderARCFOUR extends BaseBuilder<BuilderARCFOUR>{

    private volatile byte[] key = new byte[]{};
    private volatile int keySize = 1;

    /**
     * Set the encryption key and its bite size
     * Can`t be equals to 0
     * Can`t be less than 0
     */
    public BuilderARCFOUR key(String key) {
        this.key = key.getBytes();
        return this;
    }

    public BuilderARCFOUR key(String key, int keySize) {
        this.key = key.getBytes();
        this.keySize = keySize;
        return this;
    }

    public BuilderARCFOUR key(byte[] key, int keySize) {
        this.key = key;
        this.keySize = keySize;
        return this;
    }

    public BuilderARCFOUR keySize(int keySize) {
        this.keySize = keySize;
        return this;
    }

    @Override
    String encryption() throws Exception {
        return ARCFOUR.encrypt(key, keySize, message);
    }

    @Override
    String decryption() throws Exception {
        return ARCFOUR.decrypt(key, keySize, message);
    }

    @Override
    boolean hasEnoughData() {
        if(message == null){
            throw new NullPointerException(Constants.MESSAGE_EXCEPTION);
        }
        if(keySize == 0){
            throw new IllegalArgumentException(Constants.KEY_SIZE_EXCEPTION);
        } else if (keySize < 0){
            throw new IllegalArgumentException(Constants.LESS_ZERO_KEY_SIZE_EXCEPTION);
        }
        return true;
    }
}
