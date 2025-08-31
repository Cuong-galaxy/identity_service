package com.helloSpring.identity_service.validator;



/*
    Dùng để đánh dấu ràng buộc cho ngày sinh (Date of Birth - DOB).
    Có thể được sử dụng cùng với các annotation khác để thực hiện các kiểm tra tùy chỉnh.
    Ví dụ: @DobContraint @Past để đảm bảo ngày sinh là một ngày trong quá khứ.

 */


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DobValidator.class}) // Chỉ định lớp validator sẽ xử lý ràng buộc này
public @interface DobConstraint {

    // Thông điệp lỗi mặc định khi ràng buộc không hợp lệ
    String message() default "Invalid date of birth";

    // Giá trị tối thiểu (min) để kiểm tra ngày sinh
    int min();

    // Các nhóm ràng buộc (nếu có)
    Class<?>[] groups() default {};

    // Các payload bổ sung (nếu có)
    Class<? extends Payload>[] payload() default {};


}
