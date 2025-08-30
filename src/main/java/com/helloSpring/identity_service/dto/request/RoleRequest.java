package com.helloSpring.identity_service.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Data
@NoArgsConstructor //Tạo contructor không đối số
@AllArgsConstructor // tạo contructor toàn đối số
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name ;
    String description;
    Set<String> permissions;

}