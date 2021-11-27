package ar.edu.itba.paw.webapp.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HireNetAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger hirenetAccessDeniedLogger = LoggerFactory.getLogger(HireNetAccessDeniedHandler.class);


    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            hirenetAccessDeniedLogger.debug("Current user {} attempted to access {}", auth.getName(),httpServletRequest.getRequestURI());
        }

        StringBuilder builder = new StringBuilder(httpServletRequest.getContextPath());
        builder.append("/404");

        httpServletResponse.sendRedirect(builder.toString());
    }
}
