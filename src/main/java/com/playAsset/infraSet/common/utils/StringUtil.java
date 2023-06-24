package com.playAsset.infraSet.common.utils;

public class StringUtil {

    /**
     * String으로 받은 컬럼명을 CamelCase로 만든다.
     * @param fieldName
     * @return
     * @throws Exception
     */
    public static String changeCamelCase(String fieldName) throws Exception {

        String output = "";
        if (isEmpty(fieldName)) {
            throw new Exception("필드명이 비어있습니다.");
        }
        String tmp = fieldName.toLowerCase();
        for (String str : tmp.split("_")) {
            if (isEmpty(output))
                output += str;
            else
                output = output + str.substring(0, 1).toUpperCase() + str.substring(1);
        }        
        return output;
    }

    public static boolean isEmpty(String str) throws Exception {
        if (str == null
            || str.length() == 0)
            return true;
        else return false;
    }

    public static boolean isEmpty(int number) throws Exception {
        if (number == 0)
            return true;
        else return false;
    }
}
