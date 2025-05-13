package helper;

import java.text.DecimalFormat;

public class Formater {
    public static String FormatVND(double value) {
        DecimalFormat formatter = new DecimalFormat("#,### VND");
        return formatter.format(value);
    }
}
