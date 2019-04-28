package pl.prv.techalive.core;

public interface Encryptable {
    String encrypt(String text);
    String decrypt(String text);
    int getLengthMultiple();
}
