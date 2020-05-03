package ru.kc4kt4.reactive.crud.service.impl;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.kc4kt4.reactive.crud.domain.Profile;
import ru.kc4kt4.reactive.crud.repository.ProfileRepository;
import ru.kc4kt4.reactive.crud.service.ProfileRepositoryService;

import java.util.Objects;

@Service
@AllArgsConstructor
public class ProfileRepositoryServiceImpl implements ProfileRepositoryService {
    private final ProfileRepository repository;

    @Override
    public Flux<Profile> all() {
        return repository.findAll();
    }

    @Override
    public Mono<Profile> findById(@NotNull String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Profile> createOrUpdate(@NotNull Profile profile) {
        return repository.save(profile);
    }

    @Override
    public Mono<Profile> delete(@NotNull String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.empty())
                .filter(Objects::nonNull)
                .flatMap(studentToBeDeleted -> repository.delete(studentToBeDeleted)
                        .then(Mono.just(studentToBeDeleted)));
    }
}
