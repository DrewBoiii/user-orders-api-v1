package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserReactiveRepository userReactiveRepository;

    public Mono<User> getUser(String id) {
        log.info("Retrieving user {} ...", id);
        return userReactiveRepository.findById(id);
    }

}
