package ru.kc4kt4.reactive.crud.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebFilter;
import ru.kc4kt4.reactive.crud.service.ProfileHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class ProfileEndpointConfiguration {

    @Bean
    RouterFunction<ServerResponse> profiles(@NotNull ProfileHandler handler) {
        return route(GET("/profiles").and(accept(APPLICATION_JSON)), handler::all)
                .andRoute(GET("/profiles/{id}").and(accept(APPLICATION_JSON)), handler::getById)
                .andRoute(DELETE("/profiles/{id}").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), handler::deleteById)
                .andRoute(POST("/profiles").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), handler::save);
    }

    @Bean
    WebFilter errorHandler() {
        return (exchange, next) -> next.filter(exchange)
                .onErrorResume(DuplicateKeyException.class, e -> {
                    log.warn("error creating profile by duplicate id", e);
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
                    return response.setComplete();
                });
    }
}
