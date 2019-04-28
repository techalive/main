package pl.prv.techalive.encoder.hashes;

/**
 * The Adler32 Message-Digest Algorithm
 */

class Adler32 {

    public String getHash(String input){

        byte[] bytes = input.getBytes();

        short s1 = 1;
        short s2 = 0;

        int size = bytes.length;

        for (int i = 0; i < size; i++){
            s1 += bytes[i];
            s2 += s1;

        }

        s1 %= 65521;
        s2 %= 65521;

        return getOutput(s2 * 65536 + s1);
    }

    private String getOutput(int i){

        return Integer.toHexString(i);
    }

}
