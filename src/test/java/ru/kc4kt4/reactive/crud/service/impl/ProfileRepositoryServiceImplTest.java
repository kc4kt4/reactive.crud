package ru.kc4kt4.reactive.crud.service.impl;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.kc4kt4.reactive.crud.domain.Profile;
import ru.kc4kt4.reactive.crud.service.ProfileRepositoryService;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class ProfileRepositoryServiceImplTest {
    private static final String COLLECTION_NAME = "profile";

    @SuppressWarnings("rawtypes")
    @Container
    private static final GenericContainer mongo = new FixedHostPortGenericContainer("mongo:latest")
            .withFixedExposedPort(27017, 27017)
            .withEnv("MONGO_INITDB_DATABASE", "profile");

    @Autowired
    private ProfileRepositoryService service;
    @Autowired
    private ReactiveMongoOperations operations;

    @Test
    void container() {
        assertTrue(mongo.isRunning(), "container is running");
    }

    @AfterEach
    void resetDb() {
        operations.collectionExists(COLLECTION_NAME)
                .flatMap(exists -> exists ? operations.dropCollection(COLLECTION_NAME) : Mono.empty())
                .flatMap(o -> operations.createCollection(COLLECTION_NAME))
                .then()
                .block();
    }

    @Test
    void all() {
        final Profile profile1 = new Profile("1", "f_name", "s_name", "m@gmail.com");
        final Profile profile2 = new Profile("2", "f_name1", "s_name1", "m1@gmail.com");
        operations.save(Mono.just(profile1), COLLECTION_NAME)
                .then()
                .block();
        operations.save(Mono.just(profile2), COLLECTION_NAME)
                .then()
                .block();

        //test
        final List<Profile> result = service.all().collectList().block();

        assertThat(result)
                .hasSize(2)
                .element(0)
                .isEqualTo(profile1);
        assertThat(result)
                .element(1)
                .isEqualTo(profile2);
    }

    @Test
    void findById() {
        final Profile profile1 = new Profile("1", "f_name", "s_name", "m@gmail.com");
        operations.save(Mono.just(profile1))
                .then()
                .block();

        //test
        final Profile result = service.findById(profile1.getId()).block();

        assertThat(result).isNotNull().isEqualTo(profile1);
    }

    @Test
    void create() {
        final String id = new ObjectId().toString();
        final Profile profile1 = new Profile(id, "f_name", "s_name", "m@gmail.com");

        //test
        service.createOrUpdate(profile1).block();

        StepVerifier
                .create(operations.findById(id, Profile.class, COLLECTION_NAME))
                .expectNextMatches(profile -> profile.equals(profile1))
                .verifyComplete();
    }

    @Test
    void update() {
        final Profile profile1 = new Profile("1", "f_name", "s_name", "m@gmail.com");
        operations.save(Mono.just(profile1))
                .then()
                .block();

        //test
        StepVerifier
                .create(service.findById(profile1.getId()))
                .expectNextMatches(profile -> profile.equals(profile1))
                .verifyComplete();
    }

    @Test
    void delete() {
        final String id = new ObjectId().toString();
        final Profile profile1 = new Profile(id, "f_name", "s_name", "m@gmail.com");
        operations.save(Mono.just(profile1))
                .then()
                .block();

        //test
        final Profile deleted = service.delete(profile1.getId()).block();

        assertThat(deleted).isEqualTo(profile1);
        final Profile profile = operations.findAll(Profile.class).blockFirst();
        assertThat(profile).isNull();
    }
}