package pl.prv.techalive.encoder.utils;

import java.util.List;

/**
 * Class helper for working with arrays
 */

public class ArrayUtils {

    public static int[] mergeArrays(byte[] mainArray, int[] appendArray){

        int[] array = new int[mainArray.length + appendArray.length];

        int mainSize = mainArray.length;
        int appendSize = appendArray.length;

        for(int i = 0; i < mainSize; i++){
            array[i] = mainArray[i];
        }

        System.arraycopy(appendArray, 0, array, mainSize, appendSize);

        return array;
    }

    public static int[] mergeArrays(int[] mainArray, int[] appendArray){
        int[] array = new int[mainArray.length + appendArray.length];

        int mainSize = mainArray.length;
        int appendSize = appendArray.length;

        System.arraycopy(mainArray, 0, array, 0, mainSize);

        System.arraycopy(appendArray, 0, array, mainSize, appendSize);

        return array;
    }

    public static int[] convertListToArray(List<Integer> integerList){
        int[] array = new int[integerList.size()];

        for(int i = 0; i < integerList.size(); i++){
            array[i] = integerList.get(i);
        }

        return array;
    }
}
