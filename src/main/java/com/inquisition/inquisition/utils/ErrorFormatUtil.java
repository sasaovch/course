package com.inquisition.inquisition.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ErrorFormatUtil {
    private ErrorFormatUtil() {}

    public static String getPsqlExceptionMess(String ex) {
            Pattern pattern = Pattern.compile("ERROR: (.*)\\n");
            Matcher matcher = pattern.matcher(ex);

            if (matcher.find()) {
                return matcher.group(1);
            } else {
                return ex;
            }
        }
}
