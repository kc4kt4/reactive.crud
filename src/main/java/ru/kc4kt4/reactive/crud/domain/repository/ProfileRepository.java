package ru.kc4kt4.reactive.crud.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.kc4kt4.reactive.crud.domain.Profile;

public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}
