package pl.prv.techalive.encoder.callbacks;

/**
 * Callback for getting the result of encryption, decryption or key generation
 */
public interface EncodeCallback {

    void onSuccess(String result);

    void onFailure(Throwable e);
}
