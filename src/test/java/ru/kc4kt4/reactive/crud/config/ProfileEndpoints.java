package ru.kc4kt4.reactive.crud.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.kc4kt4.reactive.crud.domain.Profile;
import ru.kc4kt4.reactive.crud.service.ProfileService;
import ru.kc4kt4.reactive.crud.service.impl.ProfileHandlerImpl;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ProfileEndpointConfiguration.class, ProfileHandlerImpl.class})
@WebFluxTest
@ActiveProfiles("test")
class ProfileEndpoints {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private WebTestClient client;
    @MockBean
    private ProfileService service;

    @BeforeEach
    void setUp() {
        reset(service);
    }

    @AfterEach
    void verifyNoMore() {
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAll() throws Exception {
        final Profile profile1 = new Profile("1", "fname1", "sname1", "email1");
        final Profile profile2 = new Profile("2", "fname2", "sname2", "email2");
        when(service.all()).thenReturn(Flux.just(profile1, profile2));

        final byte[] content = client
                .get()
                .uri("/profiles")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(new ParameterizedTypeReference<>() {
                })
                .getResponseBodyContent();

        final List<Profile> profiles = mapper.readValue(new String(Objects.requireNonNull(content)), new TypeReference<>() {
        });
        assertThat(profiles).hasSize(2)
                .element(0).isEqualTo(profile1);
        assertThat(profiles)
                .element(1).isEqualTo(profile2);
        verify(service, times(1)).all();
    }

    @Test
    void save() throws Exception {
        final Profile profile = new Profile("1", "A", "B", "C");
        when(service.createOrUpdate(profile)).thenReturn(Mono.just(profile));

        //test
        final byte[] content = client
                .post()
                .uri("/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(profile), Profile.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(new ParameterizedTypeReference<>() {
                })
                .getResponseBodyContent();

        final Profile profiles = mapper.readValue(new String(Objects.requireNonNull(content)), Profile.class);
        assertThat(profiles).isEqualTo(profile);
        verify(service, times(1)).createOrUpdate(profile);
    }

    @Test
    void delete() throws Exception {
        final Profile profile = new Profile("1", "A", "B", "C");
        when(service.delete(profile.getId())).thenReturn(Mono.just(profile));

        //test
        final byte[] content = client
                .delete()
                .uri("/profiles/" + profile.getId())
                .exchange()
                .expectStatus().isOk()
                .returnResult(new ParameterizedTypeReference<>() {
                })
                .getResponseBodyContent();

        final Profile profiles = mapper.readValue(new String(Objects.requireNonNull(content)), Profile.class);
        assertThat(profiles).isEqualTo(profile);
        verify(service, times(1)).delete(profile.getId());
    }

    @Test
    void getById() throws Exception {
        final Profile profile = new Profile("1", "A", "B", "C");
        when(service.findById(profile.getId())).thenReturn(Mono.just(profile));

        //test
        final byte[] content = client
                .get()
                .uri("/profiles/" + profile.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(new ParameterizedTypeReference<>() {
                })
                .getResponseBodyContent();

        final Profile profiles = mapper.readValue(new String(Objects.requireNonNull(content)), Profile.class);
        assertThat(profiles).isEqualTo(profile);
        verify(service, times(1)).findById(profile.getId());
    }
}