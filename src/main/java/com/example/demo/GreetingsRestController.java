package com.example.demo;

import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;


@RestController
@RequestMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Profile("classic")
public class GreetingsRestController {

    private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
    private final ProfileService profileService;

    public GreetingsRestController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    Publisher<com.example.demo.Profile> getAll() {
        return this.profileService.all();
    }

    @GetMapping("/{id}")
    Publisher<com.example.demo.Profile> getById(@PathVariable("id") String id) {
        return this.profileService.get(id);
    }

    @PostMapping
    Publisher<ResponseEntity<com.example.demo.Profile>> create(@RequestBody com.example.demo.Profile profile) {
        return this.profileService.create(profile.getEmail())
                                  .map(p -> ResponseEntity.created(URI.create("/profiles/" + p.getId()))
                                  .contentType(mediaType).build());
    }

    @PutMapping("/{id}")
    Publisher<ResponseEntity<com.example.demo.Profile>> updateById(@PathVariable("id") String id, @RequestBody com.example.demo.Profile profile) {
        return Mono.just(profile)
                   .flatMap(p -> this.profileService.update(id, p.getEmail()))
                   .map(p -> ResponseEntity.ok().contentType(mediaType).build());
    }
}
