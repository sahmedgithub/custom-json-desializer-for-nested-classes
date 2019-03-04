package com.example.demo;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class ProfileService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ProfileRepository profileRepository;

    public ProfileService(ApplicationEventPublisher applicationEventPublisher, ProfileRepository profileRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.profileRepository = profileRepository;
    }

    public Flux<Profile> all() {
        return this.profileRepository.findAll();
    }

    public Mono<Profile> get(String id) {
        return profileRepository.findById(id);
    }

    public Mono<Profile> update(String id, String email) {
        return profileRepository.findById(id)
                                .map(profile -> new Profile(profile.getId(), email))
                                .flatMap(this.profileRepository::save);
    }

    public Mono<Profile> delete(String id) {
        return this.profileRepository.findById(id)
                                     .flatMap(profile -> this.profileRepository.delete(profile)
                                     .thenReturn(profile));
    }

    public Mono<Profile> create(String email) {
        return this.profileRepository.save(new Profile(null, email)).doOnSuccess(profile -> this.applicationEventPublisher.publishEvent(new ProfileCreatedEvent(profile)));
    }
}
