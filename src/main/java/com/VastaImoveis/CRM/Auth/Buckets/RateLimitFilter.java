package com.VastaImoveis.CRM.Auth.Buckets;

import com.VastaImoveis.CRM.Auth.utils.IpUtils;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if (
                !request.getMethod().equals("POST") ||
                        !request.getRequestURI().equals("/public/leads")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = IpUtils.getClientIp(request);


        System.out.println("================================");
        System.out.println("RemoteAddr: " + request.getRemoteAddr());
        System.out.println("X-Forwarded-For: " + request.getHeader("X-Forwarded-For"));
        System.out.println("X-Real-IP: " + request.getHeader("X-Real-IP"));
        System.out.println("IP FINAL: " + ip);
        System.out.println("================================");

        Bucket bucket = rateLimitService.resolveBucket(ip);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.getWriter().write("""
                    {
                        "success": false,
                        "message": "Muitas tentativas. Aguarde alguns instantes."
                    }
                """);
    }
}