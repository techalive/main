package pl.prv.techalive.encoder.hashes;

import java.util.ArrayList;
import java.util.List;

import pl.prv.techalive.encoder.utils.ArrayUtils;

/**
 * The RIPEMD-320 Message-Digest Algorithm
 * (RACE Integrity Primitives Evaluation Message Digest)
 */
@SuppressWarnings("all")
class RIPEMD_320 {

    /**
     * Message Digest Buffer
     */
    private int AL;
    private int BL;
    private int CL;
    private int DL;
    private int EL;

    private int AR;
    private int BR;
    private int CR;
    private int DR;
    private int ER;

    /**
     * Shifts for left line
     */
    private static final int[] S = {
            11, 14, 15, 12,  5,  8,  7,  9, 11, 13, 14, 15,  6,  7,  9,  8,
            7,  6,  8, 13, 11,  9,  7, 15,  7, 12, 15,  9, 11,  7, 13, 12,
            11, 13,  6,  7, 14,  9, 13, 15, 14,  8, 13,  6,  5, 12,  7,  5,
            11, 12, 14, 15, 14, 15,  9,  8,  9, 14,  5,  6,  8,  6,  5, 12,
            9, 15,  5, 11,  6,  8, 13, 12,  5, 12, 13, 14, 11,  8,  5,  6
    };

    /**
     * Shifts for right line
     */
    private static final int[] SS = {
            8,  9,  9, 11, 13, 15, 15,  5,  7,  7,  8, 11, 14, 14, 12,  6,
            9, 13, 15,  7, 12,  8,  9, 11,  7,  7, 12,  7,  6, 15, 13, 11,
            9,  7, 15, 11,  8,  6,  6, 14, 12, 13,  5, 14, 13, 13,  7,  5,
            15, 5,  8, 11, 14, 14,  6, 14,  6,  9, 12,  9, 12,  5, 15,  8,
            8,  5, 12,  9, 12,  5, 14,  6,  8, 13,  6,  5, 15, 13, 11, 11
    };

    /**
     * Index of 32-bits word in left line
     */
    private static final int[] X = {
            0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
            7,  4, 13,  1, 10,  6, 15,  3, 12,  0,  9,  5,  2, 14, 11,  8,
            3, 10, 14,  4,  9, 15,  8,  1,  2,  7,  0,  6, 13, 11,  5, 12,
            1,  9, 11, 10,  0,  8, 12,  4, 13,  3,  7, 15, 14,  5,  6,  2,
            4,  0,  5,  9,  7, 12,  2, 10, 14,  1,  3,  8, 11,  6, 15, 13,
    };

    /**
     * Index of 32-bits word in right line
     */
    private static final int[] XX = {
            5, 14,  7,  0,  9,  2, 11,  4, 13,  6, 15,  8,  1, 10,  3, 12,
            6, 11,  3,  7,  0, 13,  5, 10, 14, 15,  8, 12,  4,  9,  1,  2,
            15,  5,  1,  3,  7, 14,  6,  9, 11,  8, 12,  2, 10,  0,  4, 13,
            8,  6,  4,  1,  3, 11, 15,  0,  5, 12,  2, 13,  9,  7, 10, 14,
            12, 15, 10,  4,  1,  5,  8,  7,  6,  2, 13, 14,  0,  3,  9, 11
    };

    /**
     * This method return hash of message
     */
    public String getHash(String input){
        byte[] bytes = input.getBytes();

        List<Integer> intList = appendPaddingBits(bytes);

        intList = appendLength(intList, bytes.length * 8);

        int[] array = ArrayUtils.convertListToArray(intList);

        initMdBuffer();

        array = processHash(array);

        return getOutput(array);
    }

    /**
     * Step 1. Append Padding Bits
     *
     * The message is "padded" (extended) so that its length (in bits) is
     * congruent to 448, modulo 512. That is, the message is extended so
     * that it is just 64 bits shy of being a multiple of 512 bits long.
     * Padding is always performed, even if the length of the message is
     * already congruent to 448, modulo 512.
     *
     * Padding is performed as follows: a single "1" bit is appended to the
     * message, and then "0" bits are appended so that the length in bits of
     * the padded message becomes congruent to 448, modulo 512. In all, at
     * least one bit and at most 512 bits are appended.
     *
     */
    private List<Integer> appendPaddingBits(byte[] bytes){
        List<Integer> intList = new ArrayList<>();

        for (byte aByte : bytes) {
            intList.add((int) aByte);
        }

        int one = 128;
        int zero = 0;

        intList.add(one);

        while (intList.size() % 64 != 56){
            intList.add(zero);
        }

        return intList;
    }

    /**
     * Step 2. Append Length
     *
     * A 64-bit representation of b (the length of the message before the
     * padding bits were added) is appended to the result of the previous
     * step. In the unlikely event that b is greater than 2^64, then only
     * the low-order 64 bits of b are used. (These bits are appended as two
     * 32-bit words and appended low-order word first in accordance with the
     * previous conventions.)
     *
     * At this point the resulting message (after padding with bits and with
     * b) has a length that is an exact multiple of 512 bits. Equivalently,
     * this message has a length that is an exact multiple of 16 (32-bit)
     * words. Let M[0 ... N-1] denote the words of the resulting message,
     * where N is a multiple of 16.
     *
     */
    private List<Integer> appendLength(List<Integer> intList, int length){
        List<Integer> integers = new ArrayList<>();

        for (int i = 0; i < intList.size(); i += 4) {
            integers.add(intList.get(i) | intList.get(i + 1) << 8 | intList.get(i + 2) << 16 | intList.get(i + 3) << 24);
        }

        integers.add(length);
        integers.add(0);

        return integers;
    }

    /**
     * Step 3. Initialize Message Digest Buffer
     *
     * A four-word buffer (A,B,C,D,C) is used to compute the message digest.
     * Here each of A, B, C, D, C is a 32-bit register. These registers are
     * initialized to the following values in hexadecimal, low-order bytes
     * first):
     *
     * word AL: 01 23 45 67
     * word BL: 89 ab cd ef
     * word CL: fe dc ba 98
     * word DL: 76 54 32 10
     *
     * word AR: 10 32 54 67
     * word BR: 98 BA DC FE
     * word CR: EF CD AB 89
     * word DR: 67 45 23 10
     *
     */
    private void initMdBuffer(){
        AL = 0x67452301;
        BL = 0xEFCDAB89;
        CL = 0x98BADCFE;
        DL = 0x10325476;
        EL = 0xC3D2E1F0;

        AR = 0x76543210;
        BR = 0xFEDCBA98;
        CR = 0x89ABCDEF;
        DR = 0x01234567;
        ER = 0x3C2D1E0F;
    }

    /**
     * Step 4. Process Message in 16-Word Blocks
     */
    private int[] processHash(int[] array){

//        paramseters for left line
        int AAL, BBL, CCL, DDL, EEL;

//        paramseters for right line
        int AAR, BBR, CCR, DDR, EER;

        int[] x = new int[16];

        int length = array.length;

        for(int i = 0; i < length / 16; i++){

//            Copy block i into X
            System.arraycopy(array, i * 16, x, 0, 16);

//            init parameters
            AAL = AL;
            BBL = BL;
            CCL = CL;
            DDL = DL;
            EEL = EL;

            AAR = AR;
            BBR = BR;
            CCR = CR;
            DDR = DR;
            EER = ER;

            for(int j = 0; j <= 79; j++){

                int T = FF(j, AAL, BBL, CCL, DDL, x[X[j]], S[j]) + EEL;
                AAL = EEL;
                EEL = DDL;
                DDL = rol(CCL, 10);
                CCL = BBL;
                BBL = T;

                T = FFF(79 - j, AAR, BBR, CCR, DDR, x[XX[j]], SS[j]) + EER;
                AAR = EER;
                EER = DDR;
                DDR = rol(CCR, 10);
                CCR = BBR;
                BBR = T;

                if(j == 15){
                    T = BBL;
                    BBL = BBR;
                    BBR = T;
                } else if(j == 31){
                    T = DDL;
                    DDL = DDR;
                    DDR = T;
                } else if(j == 47){
                    T = AAL;
                    AAL = AAR;
                    AAR = T;
                } else if(j == 63){
                    T = CCL;
                    CCL = CCR;
                    CCR = T;
                } else if(j == 79){
                    T = EEL;
                    EEL = EER;
                    EER = T;
                }
            }

//            Converting result
            AL = AL + AAL;
            BL = BL + BBL;
            CL = CL + CCL;
            DL = DL + DDL;
            EL = EL + EEL;

            AR = AR + AAR;
            BR = BR + BBR;
            CR = CR + CCR;
            DR = DR + DDR;
            ER = ER + EER;
        }

        return new int[]{AL, BL, CL, DL, EL, AR, BR, CR, DR, ER};
    }

    /**
     * Boolean functions for rounds
     */
    private int F(int x, int y, int z, int i){
        if(i >= 0 && i <= 15){
            return ((x) ^ (y) ^ (z));

        } else if(i >= 16 && i <= 31){
            return (((x) & (y)) | (~(x) & (z)));

        } else if (i >= 32 && i <= 47){
            return (((x) | ~(y)) ^ (z));

        } else if (i >= 48 && i <= 63){
            return (((x) & (z)) | ((y) & ~(z)));

        } else {
            return ((x) ^ ((y) | ~(z)));
        }
    }

    /**
     * Rounds for left line
     */
    private int FF(int i, int a, int b, int c, int d, int x, int s){
        a += F(b, c, d, i) + (x);

        if(i >= 16 && i <= 31){
            a += 0x5A827999;

        } else if (i >= 32 && i <= 47){
            a += 0x6ED9EBA1;

        } else if (i >= 48 && i <= 63){
            a += 0x8F1BBCDC;

        } else if (i >= 64 && i <= 79){
            a += 0xA953FD4E;
        }
        return rol(a , s);
    }


    /**
     * Rounds for right line
     */
    private int FFF(int i, int a, int b, int c, int d, int x, int s){
        a += F(b, c, d, i) + (x);

        if(i >= 16 && i <= 31){
            a += 0x7A6D76E9;

        } else if (i >= 32 && i <= 47){
            a += 0x6D703EF3;

        } else if (i >= 48 && i <= 63){
            a += 0x5C4DD124;

        } else if (i >= 64 && i <= 79){
            a += 0x50A28BE6;
        }
        return rol(a , s);
    }

    private int rol(int x, int y){
        return ((x)<<(y))|((x)>>>(32-y));
    }

    /**
     * Method for converting AL, BL, CL, DL, AR, BR, CR, DR params to hex for getting hash
     */
    private String getOutput(int[] array){
        StringBuilder hexString = new StringBuilder();

        for(int i = 0; i < 10; i++){
            String h = Integer.toHexString(changeEndianness(array[i]));

            while (h.length() < 8){
                h = "0" + h;
            }
            hexString.append(h);
        }

        return hexString.toString();
    }

    private int changeEndianness(int x){
        return ((x & 0xFF) << 24) | ((x & 0xFF00) << 8) | ((x & 0xFF0000) >>> 8) | ((x & 0xFF000000) >>> 24);
    }

}
