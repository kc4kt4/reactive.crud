package ru.kc4kt4.reactive.crud.service.impl;

import org.bson.types.ObjectId;
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
import reactor.test.StepVerifier;
import ru.kc4kt4.reactive.crud.domain.Profile;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class MongoProfileServiceTest {
    private static final String COLLECTION_NAME = "profile";

    @SuppressWarnings("rawtypes")
    @Container
    private static final GenericContainer mongo = new FixedHostPortGenericContainer("mongo:latest")
            .withFixedExposedPort(27017, 27017)
            .withEnv("MONGO_INITDB_DATABASE", "profile");
    @Autowired
    private MongoProfileService service;
    @Autowired
    private ReactiveMongoOperations mongoOperations;

    @Test
    void container() {
        assertTrue(mongo.isRunning(), "container is running");
    }

    @Test
    void all() {
        final Profile profile1 = new Profile("1", "f_name", "s_name", "m@gmail.com");
        final Profile profile2 = new Profile("2", "f_name1", "s_name1", "m1@gmail.com");
        mongoOperations.save(profile1, COLLECTION_NAME).block();
        mongoOperations.save(profile2, COLLECTION_NAME).block();

        try {
            //test
            StepVerifier
                    .create(service.all())
                    .expectNextMatches(profile -> profile.equals(profile1))
                    .expectNextMatches(profile -> profile.equals(profile2))
                    .verifyComplete();
        } finally {
            mongoOperations.remove(profile1, COLLECTION_NAME).block();
            mongoOperations.remove(profile2, COLLECTION_NAME).block();
        }
    }

    @Test
    void findById() {
        final Profile profile1 = new Profile("1", "f_name", "s_name", "m@gmail.com");
        mongoOperations.save(profile1, COLLECTION_NAME).block();

        try {
            //test
            StepVerifier
                    .create(service.findById(profile1.getId()))
                    .expectNextMatches(profile -> profile.equals(profile1))
                    .verifyComplete();
        } finally {
            mongoOperations.remove(profile1, COLLECTION_NAME).block();
        }
    }

    @Test
    void create() {
        final String id = new ObjectId().toString();
        final Profile profile1 = new Profile(id, "f_name", "s_name", "m@gmail.com");

        try {
            //test
            service.createOrUpdate(profile1).block();

            StepVerifier
                    .create(mongoOperations.findById(id, Profile.class, COLLECTION_NAME))
                    .expectNextMatches(profile -> profile.equals(profile1))
                    .verifyComplete();
        } finally {
            mongoOperations.remove(profile1, COLLECTION_NAME).block();
        }
    }

    @Test
    void update() {
        final Profile profile1 = new Profile("1", "f_name", "s_name", "m@gmail.com");
        mongoOperations.save(profile1, COLLECTION_NAME).block();

        //test
        try {
            StepVerifier
                    .create(service.findById(profile1.getId()))
                    .expectNextMatches(profile -> profile.equals(profile1))
                    .verifyComplete();
        } finally {
            mongoOperations.remove(profile1, COLLECTION_NAME).block();
        }
    }

    @Test
    void delete() {
        final String id = new ObjectId().toString();
        final Profile profile1 = new Profile(id, "f_name", "s_name", "m@gmail.com");
        mongoOperations.save(profile1, COLLECTION_NAME).block();

        try {
            //test
            final Profile deleted = service.delete(profile1.getId()).block();

            assertThat(deleted).isEqualTo(profile1);
            final Profile profile = mongoOperations.findAll(Profile.class).blockFirst();
            assertThat(profile).isNull();
        } catch (Exception e) {
            mongoOperations.remove(profile1, COLLECTION_NAME).block();
        }
    }
}