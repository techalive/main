package pl.prv.techalive.encoder.builders;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import pl.prv.techalive.encoder.threads.BaseThread;
import pl.prv.techalive.encoder.threads.KeyGenerateThread;
import pl.prv.techalive.encoder.constants.Constants;
import pl.prv.techalive.encoder.methods.RSA;
import pl.prv.techalive.encoder.utils.EncryptUtils;

/**
 * RSA Encrypt/Decrypt Builder
 */
public class BuilderRSA extends BaseBuilder<BuilderRSA>{

    private volatile RSA.Method method;

    private volatile PublicKey publicKey;
    private volatile PrivateKey privateKey;
    private volatile KeyPair key;
    private volatile RSA.KeySize keySize = RSA.setKeySize(512);

    private volatile RSA.KeyCallback keyCallback;

    /**
     * Set the encryption method for encrypting or decrypting
     */
    public BuilderRSA method(RSA.Method method) {
        this.method = method;
        return this;
    }

    /**
     * Set the encryption key and its bite size
     */
    public BuilderRSA key(KeyPair key) {
        this.key = key;
        return this;
    }

    public BuilderRSA keySize(RSA.KeySize keySize) {
        this.keySize = keySize;
        return this;
    }

    /**
     * Set private and public key for encrypting or decrypting
     */
    public BuilderRSA publicKey(String publicKey) {
        this.publicKey = EncryptUtils.getRsaPublicKey(publicKey);
        return this;
    }

    public BuilderRSA publicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public BuilderRSA privateKey(String privateKey) {
        this.privateKey = EncryptUtils.getRsaPrivateKey(privateKey);
        return this;
    }

    public BuilderRSA privateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public BuilderRSA keyCallBack(RSA.KeyCallback keyCallback) {
        this.keyCallback = keyCallback;
        return this;
    }

    /**
     * This method calls synchronous key generation
     */
    public KeyPair generateKey(){
        if(keySize == null){
            throw new NullPointerException(Constants.RSA_KEY_SIZE_EXCEPTION);
        } else {
            try {
                return RSA.generateKey(keySize);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * This method calls asynchronous key generation
     */
    public void generateKeyAsync(){
        if(keyCallback == null){
            throw new NullPointerException(Constants.RSA_KEY_CALLBACK_EXCEPTION);
        }

        BaseThread.EncodeAction<KeyPair> action = new BaseThread.EncodeAction<KeyPair> () {
            @Override
            public KeyPair action() {
                try {
                    return generateKey();
                } catch (Exception e) {
                    keyCallback.onFailure(e);
                }
                return null;
            }
        };

        new KeyGenerateThread(action, new BaseThread.ThreadCallback<KeyPair>() {
            @Override
            public void onFinish(KeyPair parameter) {
                keyCallback.onSuccess(parameter);
            }

            @Override
            public void onFailed(Throwable e) {
                keyCallback.onFailure(e);
            }
        }).start();
    }

    @Override
    String encryption() throws Exception {
        if(key != null){
            return RSA.encrypt(method, key, message, keyCallback);
        } else if(publicKey != null){
            return RSA.encrypt(method, publicKey, message, keyCallback);
        } else {
            return RSA.encrypt(method, keySize, message, keyCallback);
        }
    }

    @Override
    String decryption() throws Exception{
        if(key != null){
            return RSA.decrypt(method, key, message);
        } else if(privateKey != null){
            return RSA.decrypt(method, privateKey, message);
        } else {
            throw new NullPointerException(Constants.RSA_HAS_NOT_KEY_EXCEPTION);
        }
    }

    @Override
    boolean hasEnoughData(){
        if(method == null){
            throw new NullPointerException(Constants.METHOD_EXCEPTION);
        }
        if(message == null){
            throw new NullPointerException(Constants.MESSAGE_EXCEPTION);
        }
        return true;
    }
}
