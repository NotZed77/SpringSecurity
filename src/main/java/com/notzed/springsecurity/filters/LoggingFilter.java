package com.notzed.springsecurity.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        logRequest(httpRequest);

        ResponseWrapper responseWrapper = new ResponseWrapper(httpResponse);

        filterChain.doFilter(request, responseWrapper);

        logResponse(httpRequest, responseWrapper);

    }

    private void logRequest(HttpServletRequest request){
        logger.info("Incoming Request : [{}] {}", request.getMethod(), request.getRequestURI());
        request.getHeaderNames().asIterator().forEachRemaining(header ->
                logger.info("Header: {} = {}", header, request.getHeader(header))
            );
    }


    private void logResponse(HttpServletRequest request, ResponseWrapper responseWrapper)throws IOException {
        logger.info("Outgoing response for [{}] {}: Status = {}", request.getMethod(),
                request.getRequestURI(), responseWrapper.getStatus());
        logger.info("Response body: {}", responseWrapper.getBodyAsString());
    }
}
