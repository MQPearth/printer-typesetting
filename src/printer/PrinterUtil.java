package printer;

import java.io.UnsupportedEncodingException;

/**
 * PrinterUtil
 *
 * @author mqpearth
 * @date 2021.1.28 10:53
 */
public class PrinterUtil {

    public static byte[] getBytes(String str) {
        if (str == null) {
            return new byte[]{};
        }
        try {
            return str.getBytes(PrinterCons.CHARSET_GBK);
        } catch (UnsupportedEncodingException e) {
            return new byte[]{};
        }
    }

    public static String buildString(byte[] bytes) {
        try {
            return new String(bytes, PrinterCons.CHARSET_GBK);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static int calMaxNeedCount(int molecular, int denominator) {
        int needCount = molecular / denominator;
        if (molecular % denominator != 0) {
            needCount++;
        }
        return needCount;
    }
}
