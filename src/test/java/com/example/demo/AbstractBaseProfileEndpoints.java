package com.example.demo;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.when;

@Log4j2
@WebFluxTest
public abstract  class AbstractBaseProfileEndpoints {
    private final WebTestClient client;

    @MockBean
    private ProfileRepository profileRepository;

    protected AbstractBaseProfileEndpoints(WebTestClient webTestClient) {
        this.client = webTestClient;
    }

    @Test
    @DisplayName("Testing get all")
    void getAll() {
        log.info("Runnig " + this.getClass().getName());

        when(this.profileRepository.findAll()).thenReturn(
                Flux.just(new Profile("1", "A"), new Profile("2", "B"))
        );

        this.client
                .get()
                .uri("/profiles")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo("1")
                .jsonPath("$.[0].email").isEqualTo("A")
                .jsonPath("$.[1].id").isEqualTo("2")
                .jsonPath("$.[1].email").isEqualTo("B");
    }

    @Test
    void delete() {
        Profile profile = new Profile("123", UUID.randomUUID().toString() + "@email.com");
        when(this.profileRepository.findById(profile.getId())).thenReturn(Mono.just(profile));
        when(this.profileRepository.deleteById(profile.getId())).thenReturn(Mono.empty());

        this.client
                .delete()
                .uri("/profiles/" + profile.getId())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void update() {
        Profile data = new Profile("123", UUID.randomUUID().toString() + "@email.com");

         when(this.profileRepository.findById(data.getId()))
                .thenReturn(Mono.just(data));

        when(this.profileRepository.save(data))
                .thenReturn(Mono.just(data));

        this
                .client
                .put()
                .uri("/profiles/" + data.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(data), Profile.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getById() {

        Profile data = new Profile("1", "A");

        when(this.profileRepository.findById(data.getId()))
                .thenReturn(Mono.just(data));

        this.client
                .get()
                .uri("/profiles/" + data.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isEqualTo(data.getId())
                .jsonPath("$.email").isEqualTo(data.getEmail());
    }

}
