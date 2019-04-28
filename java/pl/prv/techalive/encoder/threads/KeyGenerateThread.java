package pl.prv.techalive.encoder.threads;

import java.security.KeyPair;


public class KeyGenerateThread extends BaseThread<KeyPair> {

    public KeyGenerateThread(EncodeAction<KeyPair> encodeAction, ThreadCallback<KeyPair> threadCallback) {
        super(encodeAction, threadCallback);
    }
}
