package ua.com.nc.utils;

public class CastUtils {

    public static  Integer castIntegerToString(String value){
        if (value.trim().equals("null") || value.equals("undefined") || value.trim().isEmpty()) {
            return null;
        } else {
            return Integer.parseInt(value);
        }
    }
}
