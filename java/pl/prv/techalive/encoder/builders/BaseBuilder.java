package pl.prv.techalive.encoder.builders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import pl.prv.techalive.encoder.threads.BaseThread;
import pl.prv.techalive.encoder.callbacks.EncodeCallback;
import pl.prv.techalive.encoder.threads.EncodingThread;

/**
 * This class implements basic function of synchronous and asynchronous encoding
 */
public abstract class BaseBuilder<B extends BaseBuilder>{

    byte[] message;

    /**
     * Callback for getting the result of encryption
     */
    private volatile EncodeCallback callback;

    /**
     * Set the message for encrypting or decrypting
     */
    public B message(byte message) {
        this.message = new byte[1];
        this.message[0] = message;

        return (B) this;
    }

    public B message(byte... message) {
        this.message = message;

        return (B) this;
    }

    public B message(short message) {
        this.message = ByteBuffer.allocate(4).putShort(message).array();
        return (B) this;
    }

    public B message(short ... message) {
        int size = message.length;
        ByteBuffer buffer = ByteBuffer.allocate(4 * size);

        for(short i : message){
            buffer.putShort(i);
        }
        this.message = buffer.array();
        return (B) this;
    }

    public B message(int message) {
        this.message = ByteBuffer.allocate(4).putInt(message).array();

        return (B) this;
    }

    public B message(int... message) {
        int size = message.length;
        ByteBuffer buffer = ByteBuffer.allocate(4 * size);

        for(int i : message){
            buffer.putInt(i);
        }
        this.message = buffer.array();
        return (B) this;
    }

    public B message(float message) {
        this.message = ByteBuffer.allocate(4).putFloat(message).array();

        return (B) this;
    }

    public B message(float... message) {
        int size = message.length;
        ByteBuffer buffer = ByteBuffer.allocate(4 * size);

        for(float i : message){
            buffer.putFloat(i);
        }
        this.message = buffer.array();
        return (B) this;
    }

    public B message(long message) {
        this.message = ByteBuffer.allocate(4).putLong(message).array();

        return (B) this;
    }

    public B message(long... message) {
        int size = message.length;
        ByteBuffer buffer = ByteBuffer.allocate(4 * size);

        for(long i : message){
            buffer.putLong(i);
        }
        this.message = buffer.array();
        return (B) this;
    }

    public B message(double message) {
        this.message = ByteBuffer.allocate(4).putDouble(message).array();

        return (B) this;
    }

    public B message(double... message) {
        int size = message.length;
        ByteBuffer buffer = ByteBuffer.allocate(4 * size);

        for(double i : message){
            buffer.putDouble(i);
        }
        this.message = buffer.array();
        return (B) this;
    }

    public B message(String message) {
        this.message = message.getBytes();

        return (B) this;
    }

    public B message(File file){
        try {
            return message(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public B message(InputStream inputStream) {
        try {
            message = new byte[inputStream.available()];
            inputStream.read(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (B) this;
    }

    /**
     * Set the callback
     */
    public BaseBuilder encryptCallBack(EncodeCallback callback){
        this.callback = callback;
        return this;
    }

    /**
     * Start of asynchronous encrypting
    */
    public void encryptAsync(){
        if(hasEnoughData()){
            BaseThread.EncodeAction action = new BaseThread.EncodeAction() {
                @Override
                public String action() {
                    try {
                        return encryption();
                    } catch (Exception e) {
                        callback.onFailure(e);
                    }
                    return null;
                }
            };

            new EncodingThread(action, new BaseThread.ThreadCallback<String>() {
                @Override
                public void onFinish(String parameter) {
                    callback.onSuccess(parameter);
                }

                @Override
                public void onFailed(Throwable e) {
                    callback.onFailure(e);
                }
            }).start();
        }
    }

    /**
     * Start of asynchronous decrypting
     */
    public void decryptAsync(){
        if(hasEnoughData()){
            BaseThread.EncodeAction action = new BaseThread.EncodeAction() {
                @Override
                public String action() {
                    try {
                        return decryption();
                    } catch (Exception e) {
                        callback.onFailure(e);
                    }
                    return null;
                }
            };

            new EncodingThread(action, new BaseThread.ThreadCallback<String>() {
                @Override
                public void onFinish(String parameter) {
                    callback.onSuccess(parameter);
                }

                @Override
                public void onFailed(Throwable e) {
                    callback.onFailure(e);
                }
            }).start();
        }
    }

    /**
     * Start of synchronous encrypting
     */
    public String encrypt(){
        try {
            if(hasEnoughData()){
                return encryption();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Start of synchronous decrypting
     */
    public String decrypt(){
        try {
            if(hasEnoughData()){
                return decryption();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Implementation of calling encryption and decryption
     */
    abstract String encryption() throws Exception ;

    abstract String decryption() throws Exception ;

    /**
     * Method for checking all set data in Builder
     */
    abstract boolean hasEnoughData();

}
