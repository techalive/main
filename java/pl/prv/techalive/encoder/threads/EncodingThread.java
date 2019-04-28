package pl.prv.techalive.encoder.threads;

public class EncodingThread extends BaseThread<String>{

    public EncodingThread(EncodeAction<String> encodeAction, ThreadCallback<String> threadCallback) {
        super(encodeAction, threadCallback);
    }
}
