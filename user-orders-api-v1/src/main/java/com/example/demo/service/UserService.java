package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService {

    private final UserReactiveRepository userReactiveRepository;

    public Mono<User> getUser(String id) {
        return userReactiveRepository.findById(id);
    }

}
