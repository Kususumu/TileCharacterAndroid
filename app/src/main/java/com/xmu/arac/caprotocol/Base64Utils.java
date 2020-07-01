package com.xmu.arac.caprotocol;

import android.util.Base64;

public class Base64Utils {
    public static byte[] decode(final byte[] bytes) {
        //return Base64.decodeBase64(bytes);
        return Base64.decode(bytes,Base64.DEFAULT);
    }

    public static String encode(final byte[] bytes) {
        //return new String(Base64.encodeBase64(bytes));
        return new String(Base64.encodeToString(bytes,Base64.DEFAULT));
    }
}