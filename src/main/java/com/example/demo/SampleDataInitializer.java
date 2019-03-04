package com.example.demo;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Log4j2
@Component
@Profile("demo")
public class SampleDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ProfileRepository profileRepository;

    public SampleDataInitializer(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        profileRepository.deleteAll()
                         .thenMany(Flux.just("A", "B", "C", "D"))
                         .map(name -> new com.example.demo.Profile(UUID.randomUUID().toString(), name + "@email.com"))
                         .thenMany(profileRepository.findAll())
                         .subscribe(profile -> log.info("saving " + profile.toString()));
    }
}
