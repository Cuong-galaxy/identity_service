package com.helloSpring.identity_service.service;

import com.helloSpring.identity_service.dto.request.AuthenticationRequest;
import com.helloSpring.identity_service.exception.AppException;
import com.helloSpring.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService {
    UserRepository userRepository;

    boolean authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> AppException())
    }
}
