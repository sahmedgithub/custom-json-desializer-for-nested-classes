package com.example.demo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}
