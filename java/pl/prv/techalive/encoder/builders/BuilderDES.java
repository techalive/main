package pl.prv.techalive.encoder.builders;

import pl.prv.techalive.encoder.constants.Constants;
import pl.prv.techalive.encoder.methods.DES;

/**
 * DES Encrypt/Decrypt Builder
 */
public class BuilderDES extends BaseBuilder<BuilderDES>{

    private volatile String method;

    private volatile byte[] key = new byte[]{};

    private volatile byte[] iVector = new byte[]{};

    /**
     * Set the encryption method for encrypting or decrypting
     */
    public BuilderDES method(DES.Method method) {
        this.method = method.getMethod();
        return this;
    }

    public BuilderDES method(DES.MethodCFB method) {
        this.method = method.getMethod();
        return this;
    }

    public BuilderDES method(DES.MethodOFB method) {
        this.method = method.getMethod();
        return this;
    }

    /**
     * Set the encryption key
     */
    public BuilderDES key(String key) {
        this.key = key.getBytes();
        return this;
    }

    public BuilderDES key(byte[] key) {
        this.key = key;
        return this;
    }

    /**
     * Set initialization vector (IV)
     */
    public BuilderDES iVector(String iVector) {
        this.iVector = iVector.getBytes();
        return this;
    }

    public BuilderDES iVector(byte[] iVector) {
        this.iVector = iVector;
        return this;
    }

    @Override
    String encryption() throws Exception {
        return DES.encrypt(method, key,iVector, message);
    }

    @Override
    String decryption() throws Exception {
        return DES.decrypt(method, key, iVector, message);
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
