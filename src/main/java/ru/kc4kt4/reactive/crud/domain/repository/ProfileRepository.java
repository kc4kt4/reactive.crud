package ru.kc4kt4.reactive.crud.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.kc4kt4.reactive.crud.domain.Profile;

import javax.validation.constraints.NotNull;

public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
    Mono<Profile> findByEmail(@NotNull final String email);
}
