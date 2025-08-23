package com.helloSpring.identity_service.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor //Tạo contructor không đối số
@AllArgsConstructor // tạo contructor toàn đối số
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectRequest {
    String token ;

}