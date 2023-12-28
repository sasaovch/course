package com.inquisition.inquisition.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@Configuration
public class RedirectFilterConfiguration {
    private final Logger logger = LoggerFactory.getLogger(RedirectFilterConfiguration.class);

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> spaRedirectFiler() {
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(createRedirectFilter());
        registration.addUrlPatterns("/*");
        registration.setName("frontendRedirectFiler");
        registration.setOrder(1);
        return registration;
    }

    private OncePerRequestFilter createRedirectFilter() {
        return new OncePerRequestFilter() {
            // Forwards all routes except '/index.html', '/200.html', '/favicon.ico', '/sw.js' '/api/', '/api/**'
            private static final String REGEX = "(/accusations|/auth|/bibles|/cases|/index\\.html|/inquisitions|/localities|/persons|.*js|.*css|.*ttf|.*woff|.*woff2|.*eot|.*jpg|.*png|.*svg|.*ico).*$";
            private final Pattern pattern = Pattern.compile(REGEX);
            @Override
            protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
                if (!pattern.matcher(req.getRequestURI()).matches() && !req.getRequestURI().equals("/")) {
                    // Delegate/Forward to `/` if `pattern` doesn't match
                    // Required because of 'mode: history'usage in frontend routing, see README for further details
                    RedirectFilterConfiguration.this.logger.info("URL {} entered directly into the Browser, redirecting...", req.getRequestURI());
                    RequestDispatcher rd = req.getRequestDispatcher("/");
                    rd.forward(req, res);
                } else {
                    chain.doFilter(req, res);
                }
            }
        };
    }
}
