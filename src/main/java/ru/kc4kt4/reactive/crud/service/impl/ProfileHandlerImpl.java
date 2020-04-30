package ru.kc4kt4.reactive.crud.service.impl;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.kc4kt4.reactive.crud.domain.Profile;
import ru.kc4kt4.reactive.crud.service.ProfileHandler;
import ru.kc4kt4.reactive.crud.service.ProfileService;

@Component
@AllArgsConstructor
public class ProfileHandlerImpl implements ProfileHandler {
    private final ProfileService profileService;

    @Override
    @NotNull
    public Mono<ServerResponse> getById(@NotNull final ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(profileService.findById(id(request)), Profile.class);
    }

    @Override
    @NotNull
    public Mono<ServerResponse> all(@NotNull final ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(profileService.all(), Profile.class);
    }

    @Override
    @NotNull
    public Mono<ServerResponse> deleteById(@NotNull final ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(profileService.delete(id(request)), Profile.class);
    }

    @Override
    @NotNull
    public Mono<ServerResponse> createAndUpdate(@NotNull final ServerRequest request) {
        Mono<Profile> profile = request
                .bodyToMono(Profile.class)
                .flatMap(profileService::createOrUpdate);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(profile, Profile.class);
    }

    @NotNull
    private String id(@NotNull final ServerRequest request) {
        return request.pathVariable("id");
    }
}
