package com.kaka.webgm.util;

import javax.servlet.http.HttpServletRequest;

public class GmUtils {
    public static String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
