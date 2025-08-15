package com.helloSpring.identity_service.service;


import com.helloSpring.identity_service.dto.request.UserCreationRequest;
import com.helloSpring.identity_service.dto.request.UserUpdateRequest;
import com.helloSpring.identity_service.entity.User;
import com.helloSpring.identity_service.exception.AppException;
import com.helloSpring.identity_service.exception.ErrorCode;
import com.helloSpring.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request){
        User user = new User();
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);


        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(String id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(String userId, UserUpdateRequest request){
        User user = getUser(userId);

        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setPassword(request.getPassword());
        user.setDob(request.getDob());

        return userRepository.save(user);

    }
    public void deleteUser(String userId ){
        userRepository.deleteById(userId);
    }

}
