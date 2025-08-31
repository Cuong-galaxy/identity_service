package com.helloSpring.identity_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/*
    * Lớp DobValidator kiểm tra tính hợp lệ của ngày sinh (dob) dựa trên ràng buộc DobConstraint.
    * Nó đảm bảo rằng người dùng phải đủ 18 tuổi trở lên.
 */

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;

    // Phương thức kiểm tra tính hợp lệ của ngày sinh
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Cho phép giá trị null, sử dụng @NotNull để kiểm tra null nếu cần
        }
        // Tính tuổi dựa trên ngày sinh
        LocalDate today = LocalDate.now();

        // Tính tuổi
        int age = today.getYear() - value.getYear();
        if (today.getDayOfYear() < value.getDayOfYear()) {
            age--; // Chưa đến sinh nhật trong năm nay
        }
        return age >= 18; // Kiểm tra tuổi >= 18
    }

    // Phương thức khởi tạo, lấy giá trị min từ annotation
    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        // Gọi phương thức khởi tạo của lớp cha (nếu cần)
        ConstraintValidator.super.initialize(constraintAnnotation);
        // Lấy giá trị min từ annotation
        min = constraintAnnotation.min();
    }

}
