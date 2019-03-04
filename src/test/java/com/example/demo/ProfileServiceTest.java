package com.example.demo;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.function.Predicate;

@Log4j2
@DataMongoTest
@Import(ProfileService.class)
class ProfileServiceTest {

    private final ProfileService profileService;
    private final ProfileRepository profileRepository;

    ProfileServiceTest(@Autowired ProfileService profileService, @Autowired ProfileRepository profileRepository) {
        this.profileService = profileService;
        this.profileRepository = profileRepository;
    }

    @Test
    void all() {
        Flux<Profile> saved = profileRepository.saveAll(Flux.just(new Profile(null, "A"), new Profile(null, "B")));

        Flux<Profile> composite = profileService.all().thenMany(saved);

        Predicate<Profile> match = profile -> saved.any(saveItem -> saveItem.equals(profile)).block();

        StepVerifier.create(composite)
                    .expectNextMatches(match)
                    .expectNextMatches(match)
                    .verifyComplete();
    }

    @Test
    void create() {
        Mono<Profile> profileMono = this.profileService.create("email@email.com");
        StepVerifier.create(profileMono)
                    .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                    .verifyComplete();
    }

    @Test
    void update() {
        Mono<Profile> update = this.profileService.create("test")
                                                  .flatMap(p -> this.profileService.update(p.getId(), "test1"));
        StepVerifier.create(update)
                    .expectNextMatches(p -> p.getEmail().equalsIgnoreCase("test1"))
                    .verifyComplete();
    }

    @Test
    void delete() {
        Mono<Profile> delete = this.profileService.create("delete")
                                                  .flatMap(p -> this.profileService.delete(p.getId()));

        StepVerifier.create(delete)
                    .expectNextMatches(profile -> profile.getEmail().equalsIgnoreCase("delete"))
                    .verifyComplete();
    }

    @Test
    void get() {
        String test = UUID.randomUUID().toString();

        Mono<Profile> get = this.profileService.create(test)
                                               .flatMap(p -> this.profileService.get(test));

        StepVerifier.create(get)
                    .expectNextMatches(profile -> StringUtils.hasText(profile.getId()) && test.equalsIgnoreCase(profile.getEmail()))
                    .verifyComplete();
    }
}