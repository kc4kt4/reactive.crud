package ru.kc4kt4.reactive.crud.service;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.kc4kt4.reactive.crud.domain.Profile;

public interface ProfileRepositoryService {
    Flux<Profile> all();

    Mono<Profile> findById(@NotNull final String id);

    Mono<Profile> createOrUpdate(@NotNull final Profile profile);

    Mono<Profile> delete(@NotNull final String id);
}
