package ru.kc4kt4.reactive.crud.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.kc4kt4.reactive.crud.service.ProfileHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProfileEndpointConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(@NotNull ProfileHandler handler) {
        return route(GET("/profiles"), handler::all)
                .andRoute(GET("/profiles/{id}"), handler::getById)
                .andRoute(DELETE("/profiles/{id}"), handler::deleteById)
                .andRoute(POST("/profiles"), handler::createAndUpdate);
    }
}
