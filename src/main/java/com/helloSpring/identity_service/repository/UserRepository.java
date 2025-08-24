package com.helloSpring.identity_service.repository;

import com.helloSpring.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String username);
    // tạo method tìm user theo username
    //optional để tránh lỗi null pointer exception
    Optional<User> findByUsername(String username);
}
