package com.helloSpring.identity_service.configuration;


import com.helloSpring.identity_service.entity.User;
import com.helloSpring.identity_service.enums.Role;
import com.helloSpring.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashSet;

@Slf4j                   //Để ghi log
@Configuration           //Đánh dấu đây là class cấu hình
@RequiredArgsConstructor //Tự động tạo contructor có các biến là final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) //Tự động define biến là private và final

/*
    Class này dùng để khởi tạo dữ liệu khi ứng dụng Spring Boot khởi động
    Cụ thể, nó sẽ kiểm tra xem có user admin mặc định không, nếu không có thì sẽ tạo user admin với username và password là "admin"
 */

public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder; // Được cấu hình trong SecurityConfig

    // Tự động chạy khi ứng dụng khởi động
    @Bean   // Vì ApplicationRunner là một functional interface nên có thể dùng lambda expression để khởi tạo
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()){
                var roles = new HashSet<String>(); // Sử dụng HashSet để tránh trùng lặp vai trò
                roles.add(Role.ADMIN.name()); // Thêm vai trò ADMIN
                // Tạo user admin mặc định
                User user = User.builder()   // Sử dụng builder để tạo đối tượng User
                        .username("admin")
                        .password(passwordEncoder.encode("admin")) // Mã hóa mật khẩu
                        //.roles(roles)
                        .build();

                userRepository.save(user); // Lưu user vào cơ sở dữ liệu
                log.warn("Created default admin user - username: admin, password: admin"); // Ghi log thông báo đã tạo user admin

            }
        };
    }
}