package com.naruto.connall;

public class ByteTransUtil {

    //十六进制字符串 转byte
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        //1去除0X
        String cutEND = hexString.replace("0X", "");
        cutEND = cutEND.replace("0x", "");
        //去除H -
        cutEND = cutEND.replace("H", "");
        cutEND = cutEND.replace("-", "");
        //2去除空格
        cutEND = cutEND.replace(" ", "");
        String result = cutEND.toUpperCase();
        int    length   = result.length() / 2;
        char[] hexChars = result.toCharArray();
        byte[] d        = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    //byte 转 十六进制字符串
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int    v  = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    //byte 合并
    public static byte[] bytesMege(byte[] cache,byte[] mege) {
        if(cache == null && mege == null)return null;
        if(cache == null)return mege;
        if(mege == null)return cache;
        byte[] result = new byte[cache.length+mege.length];
        System.arraycopy(cache, 0, result, 0, cache.length);
        System.arraycopy(mege, 0, result, cache.length, mege.length);
        return result;
    }
}
