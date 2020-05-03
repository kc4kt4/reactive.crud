package ru.kc4kt4.reactive.crud.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.kc4kt4.reactive.crud.domain.Profile;
import ru.kc4kt4.reactive.crud.service.ProfileRepositoryService;
import ru.kc4kt4.reactive.crud.service.ProfileService;

@Service
@Slf4j
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepositoryService service;

    @Override
    public Flux<Profile> all() {
        return service.all();
    }

    @Override
    public Mono<Profile> findById(@NotNull String id) {
        return service.findById(id);
    }

    @Override
    public Mono<Profile> createOrUpdate(@NotNull Profile profile) {
        return service.createOrUpdate(profile);
    }

    @Override
    public Mono<Profile> delete(@NotNull String id) {
        return service.delete(id);
    }
}
