package ru.kc4kt4.reactive.crud.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.kc4kt4.reactive.crud.domain.Profile;
import ru.kc4kt4.reactive.crud.service.ProfileRepositoryService;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = ProfileServiceImpl.class)
class MongoProfileServiceTest {

    @Autowired
    private ProfileServiceImpl service;
    @MockBean
    private ProfileRepositoryService repositoryService;

    @BeforeEach
    void setUp() {
        reset(repositoryService);
    }

    @AfterEach
    void verifyNoMore() {
        verifyNoMoreInteractions(repositoryService);
    }

    @Test
    void all() {
        final Profile profile1 = new Profile("1", "A1", "B1", "C1");
        final Profile profile2 = new Profile("2", "A2", "B2", "C2");
        final Flux<Profile> returned = Flux.just(profile1, profile2);
        when(repositoryService.all()).thenReturn(returned);

        //test
        StepVerifier
                .create(service.all())
                .expectNextMatches(profile -> profile.equals(profile1))
                .expectNextMatches(profile -> profile.equals(profile2))
                .verifyComplete();

        verify(repositoryService, times(1)).all();
    }

    @Test
    void findById() {
        final Profile profile1 = new Profile("1", "A1", "B1", "C1");
        final Mono<Profile> returned = Mono.just(profile1);
        when(repositoryService.findById(profile1.getId())).thenReturn(returned);

        //test
        StepVerifier
                .create(service.findById(profile1.getId()))
                .expectNextMatches(profile -> profile.equals(profile1))
                .verifyComplete();

        verify(repositoryService, times(1)).findById(profile1.getId());
    }

    @Test
    void createOrUpdate() {
        final Profile profile1 = new Profile("1", "A1", "B1", "C1");
        final Mono<Profile> returned = Mono.just(profile1);
        when(repositoryService.createOrUpdate(profile1)).thenReturn(returned);

        //test
        StepVerifier
                .create(service.createOrUpdate(profile1))
                .expectNextMatches(profile -> profile.equals(profile1))
                .verifyComplete();

        verify(repositoryService, times(1)).createOrUpdate(profile1);
    }

    @Test
    void delete() {
        final Profile profile1 = new Profile("1", "A1", "B1", "C1");
        final Mono<Profile> returned = Mono.just(profile1);
        when(repositoryService.delete(profile1.getId())).thenReturn(returned);

        //test
        StepVerifier
                .create(service.delete(profile1.getId()))
                .expectNextMatches(profile -> profile.equals(profile1))
                .verifyComplete();

        verify(repositoryService, times(1)).delete(profile1.getId());
    }
}