package edu.daemondev.psquare;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        System.out.println(
                "authfilter origin - " + httpServletRequest.getHeader("Origin") + " " + httpServletRequest.getMethod());

        if (httpServletRequest.getMethod().equals("OPTIONS")) {
            httpServletRequest.setAttribute("message", "corstesting");
        } else {
            String authHeader = httpServletRequest.getHeader("Authorization");
            if (authHeader != null) {
                String authHeaderArr[] = authHeader.split("Bearer ");
                if (authHeaderArr.length > 1 && authHeaderArr[1] != null) {
                    String token = authHeaderArr[1];
                    try {
                        Claims claims = Jwts.parser().setSigningKey(Constants.JWT_TOKEN_SECRET_KEY)
                                .parseClaimsJws(token).getBody();
                        httpServletRequest.setAttribute("tokenuserid", claims.get("userid").toString());
                    } catch (Exception e) {
                        System.out.println("Invalid / Expired token");
                        httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid / Expired token");
                        return;
                    }
                } else {
                    System.out.println("Authorization token must be Bearer [token]");
                    httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
                            "Authorization token must be Bearer [token]");
                    return;
                }
            } else {
                System.out.println("Authorization token must be provided");
                httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be provided");
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
