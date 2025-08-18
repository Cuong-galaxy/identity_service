package com.helloSpring.identity_service.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Data
@NoArgsConstructor //Tạo contructor không đối số
@AllArgsConstructor // tạo contructor toàn đối số
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    String username;
    String password;

}
