package com.helloSpring.identity_service.mapper;


import com.helloSpring.identity_service.dto.request.PermissionRequest;
import com.helloSpring.identity_service.dto.request.UserCreationRequest;
import com.helloSpring.identity_service.dto.request.UserUpdateRequest;
import com.helloSpring.identity_service.dto.response.PermissionResponse;
import com.helloSpring.identity_service.dto.response.UserResponse;
import com.helloSpring.identity_service.entity.Permission;
import com.helloSpring.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPemission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);




}
