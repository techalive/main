package pl.prv.techalive.encoder.hashes;

/**
 * The FNV132 Message-Digest Algorithm
 */

class FNV132 {

    private static final int FNV_32_PRIME = 0x01000193;

    public String getHash(String input){

        int hval = 0x811c9dc5;

        byte[] bytes = input.getBytes();

        int size = bytes.length;

        for (int i = 0; i < size; i++){
            hval *= FNV_32_PRIME;
            hval ^= bytes[i];

        }

        return getOutput(hval);
    }

    private String getOutput(int i){

        return Integer.toHexString(i);
    }

}
