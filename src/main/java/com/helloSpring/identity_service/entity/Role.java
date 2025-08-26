package com.helloSpring.identity_service.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data // tạo getter, setter, toString, equals, hashCode
@Builder // tạo builder pattern
@NoArgsConstructor // tạo constructor không tham số
@AllArgsConstructor // tạo constructor với tất cả tham số
@FieldDefaults(level = AccessLevel.PRIVATE) // đặt tất cả các trường thành private
@Entity // đánh dấu lớp này là một thực thể JPA
public class Role {
    @Id
    String name;
    String description;

    @ManyToMany
    Set<Permission> permissions;


}
