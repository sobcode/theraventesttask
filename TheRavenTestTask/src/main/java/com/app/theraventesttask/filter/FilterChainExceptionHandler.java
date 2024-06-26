package com.app.theraventesttask.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Filter class for handling exceptions thrown during the filter chain execution.
 *
 * This class intercepts exceptions thrown by other filters in the chain and delegates
 * to the configured HandlerExceptionResolver for handling and resolving the exceptions.
 */
@Component
public class FilterChainExceptionHandler extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;

    @Autowired
    public FilterChainExceptionHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Executes the filter chain and catches any runtime exceptions for handling.
     *
     * @param request     HttpServletRequest containing the incoming request.
     * @param response    HttpServletResponse for the outgoing response.
     * @param filterChain FilterChain for continuing the filter chain.
     * @throws ServletException Thrown if a servlet exception occurs.
     * @throws IOException      Thrown if an I/O exception occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request,response);
        } catch (RuntimeException e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}
