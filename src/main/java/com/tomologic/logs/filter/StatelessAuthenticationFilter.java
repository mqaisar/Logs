package com.tomologic.logs.filter;

import com.tomologic.logs.config.ApiKeyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is a generic filter which check for api-key in the request and compares it with the one placed at server
 * If we get wrong api-key in the request, it is not allowed to perform any operations
 * rather UNAUTHORIZED exception is throw to the client
 */
@Component
@WebFilter(urlPatterns = {"/*"}, description = "Checks for security key in request header")
class StatelessAuthenticationFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(StatelessAuthenticationFilter.class);

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        try {
            final HttpServletRequest request = (HttpServletRequest) req;
            logger.info("Requested {}: {}", request.getMethod(), request.getRequestURI());
            String apiKey = request.getHeader("api-key");
            if (apiKey==null || !apiKey.equals(ApiKeyProvider.getRootUserApiKey()))
                ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "api-key not valid");
            else
                chain.doFilter(req, res);

        } catch (Exception ex) {

        } finally {
        }
    }
}