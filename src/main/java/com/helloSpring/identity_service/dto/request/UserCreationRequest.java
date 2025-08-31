package com.helloSpring.identity_service.dto.request;

import com.helloSpring.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor //Tạo contructor không đối số
@AllArgsConstructor // tạo contructor toàn đối số
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) //Set access modifie cho các  thuộc tính thành private
public class UserCreationRequest {
    @Size(min = 8, message = "USERNAME_ERROR")
    String username;

    @Size(min = 10, message = "PASSWORD_ERROR")
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 4, message = "INVALID_DOB")
    LocalDate dob;

    List<String> roles;
}
