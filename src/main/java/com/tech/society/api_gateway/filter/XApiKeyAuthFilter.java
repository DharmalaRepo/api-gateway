package com.tech.society.api_gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

@Component
public class XApiKeyAuthFilter implements GlobalFilter, Ordered {

    @Value("${gateway.api-key}")
    private String validApiKey;

    private static final String API_KEY_HEADER = "X-API-KEY";
    //private static final String VALID_API_KEY = "my-secret-api-key"; // <-- store this securely

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String apiKey = exchange.getRequest().getHeaders().getFirst(API_KEY_HEADER);

        if (apiKey == null || !validApiKey.equals(apiKey)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange); // allow the request
    }

    @Override
    public int getOrder() {
        return 0; // run early
    }
}