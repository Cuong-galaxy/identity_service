package com.helloSpring.identity_service.configuration;


import com.helloSpring.identity_service.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

// đăng ký cấu hình bảo mật cho ứng dụng Spring Boot
// Đăng ký endpoint nào được phép truy cập mà không cần xác thực
public class SecurityConfig {
    // Định nghĩa các endpoint công khai mà không yêu cầu xác thực
    private final String[] PUBLIC_ENDPOINTS = {"/users", "/auth/token","/auth/introspect"};


    // Lấy khóa bí mật từ file cấu hình application.properties
    @Value("${jwt.signerKey}")
    private String SIGNING_KEY;


    // Cấu hình chuỗi bộ lọc bảo mật
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Cho phép tất cả các yêu cầu POST đến endpoint /users mà không cần xác thực
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                            //.requestMatchers(HttpMethod.GET, "/users") Vì đã có @PreAuthorize trong service nen không cần cấu hình ở đây nữa
                            //.hasAnyAuthority("ROLE_ADMIN") // Chỉ cho phép truy cập nếu có vai trò ADMIN
                            // .hasRole(Role.ADMIN.name()) // Chỉ cho phép truy cập nếu có vai trò ADMIN

                        //cho phép get thông tin myinfo cho user đã xác thực
                        //.requestMatchers(HttpMethod.GET, "/users/me").authenticated()
                        .anyRequest().authenticated()
                );

        // Cấu hình ứng dụng như một máy chủ tài nguyên OAuth2 sử dụng JWT để xác thực
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable); // Vô hiệu hóa CSRF để cho phép các yêu cầu POST không bị chặn


        return httpSecurity.build(); // Xây dựng và trả về chuỗi bộ lọc bảo mật đã cấu hình
    }



    // Cấu hình JwtDecoder sử dụng khóa bí mật để giải mã và xác thực JWT
    @Bean
    JwtDecoder jwtDecoder(){
        // Tạo SecretKeySpec từ chuỗi SIGNING_KEY và thuật toán HmacSHA256
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNING_KEY.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder                         //Tạo JwtDecoder sử dụng Nimbus
                .withSecretKey(secretKeySpec)           // Cấu hình khóa bí mật
                .macAlgorithm(MacAlgorithm.HS512)       // Cấu hình thuật toán mã hóa HMAC với SHA-512
                .build();                               // Xây dựng và trả về JwtDecoder đã cấu hình

    }

    // Cấu hình PasswordEncoder sử dụng thuật toán mã hóa BCrypt với độ phức tạp là 10
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Thêm tiền tố "

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}