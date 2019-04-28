package pl.prv.techalive.encoder.builders;

import pl.prv.techalive.encoder.constants.Constants;
import pl.prv.techalive.encoder.methods.DESede;

/**
 * AES Encrypt/Decrypt Builder
 */
public class BuilderDESede extends BaseBuilder<BuilderDESede>{

    private volatile DESede.Method method;

    private volatile byte[] key = new byte[]{};
    private volatile DESede.Key keySize = DESede.Key.SIZE_128;

    private volatile byte[] iVector = new byte[]{};

    /**
     * Set the encryption method for encrypting or decrypting
     */
    public BuilderDESede method(DESede.Method method) {
        this.method = method;
        return this;
    }

    /**
     * Set the encryption key and its bite size (128, 192)
     */
    public BuilderDESede key(String key, DESede.Key keySize) {
        this.key = key.getBytes();
        this.keySize = keySize;
        return this;
    }

    public BuilderDESede key(String key) {
        this.key = key.getBytes();
        return this;
    }

    public BuilderDESede key(byte[] key, DESede.Key keySize) {
        this.key = key;
        this.keySize = keySize;
        return this;
    }

    public BuilderDESede key(byte[] key) {
        this.key = key;
        return this;
    }

    public BuilderDESede keySize(DESede.Key keySize){
        this.keySize = keySize;
        return this;
    }

    /**
     * Set initialization vector (IV)
     */
    public BuilderDESede iVector(String iVector) {
        this.iVector = iVector.getBytes();
        return this;
    }

    public BuilderDESede iVector(byte[] iVector) {
        this.iVector = iVector;
        return this;
    }


    @Override
    String encryption() throws Exception {
        return DESede.encrypt(method, key, keySize, iVector, message);
    }

    @Override
    String decryption() throws Exception {
        return DESede.decrypt(method, key, keySize, iVector, message);
    }

    @Override
    boolean hasEnoughData() {
        if(message == null){
            throw new NullPointerException(Constants.MESSAGE_EXCEPTION);
        }
        if(method == null){
            throw new NullPointerException(Constants.METHOD_EXCEPTION);
        }
        return true;
    }
}
