package com.VastaImoveis.CRM.Auth.utils;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtils {

    public static String getClientIp(HttpServletRequest request) {

        String cfIp = request.getHeader("CF-Connecting-IP");

        if (cfIp != null && !cfIp.isBlank()) {
            return cfIp.trim();
        }

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");

        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }

        return request.getRemoteAddr();
    }
}