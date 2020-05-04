package ru.kc4kt4.reactive.crud.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ProfileHandler {
    @NotNull
    Mono<ServerResponse> getById(@NotNull ServerRequest request);

    @NotNull
    Mono<ServerResponse> all(@NotNull ServerRequest request);

    @NotNull
    Mono<ServerResponse> deleteById(@NotNull ServerRequest request);

    @NotNull
    Mono<ServerResponse> save(@NotNull ServerRequest request);
}
