package co.analisys.biblioteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean("restTemplateWithAuth")
    public RestTemplate restTemplateWithAuth() {
        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof JwtAuthenticationToken) {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
                String token = jwtAuth.getToken().getTokenValue();
                request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            }
            return execution.execute(request, body);
        };

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(authInterceptor);
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }
}
