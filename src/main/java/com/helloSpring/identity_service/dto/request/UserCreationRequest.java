package com.helloSpring.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@NoArgsConstructor //Tạo contructor không đối số
@AllArgsConstructor // tạo contructor toàn đối số
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) //Set access modifie cho các  thuộc tính thành private
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_ERROR")
    String username;

    @Size(min = 8, message = "PASSWORD_ERROR")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;


}
