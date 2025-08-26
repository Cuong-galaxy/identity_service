package com.helloSpring.identity_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // tạo getter, setter, toString, equals, hashCode
@Builder // tạo builder pattern
@NoArgsConstructor // tạo constructor không tham số
@AllArgsConstructor // tạo constructor với tất cả tham số
@FieldDefaults(level = AccessLevel.PRIVATE) // đặt tất cả các trường thành private
@Entity // đánh dấu lớp này là một thực thể JPA
public class Permission {
    @Id
    String name;
    String description;

}
