package com.helloSpring.identity_service.mapper;


import com.helloSpring.identity_service.dto.request.PermissionRequest;
import com.helloSpring.identity_service.dto.response.PermissionResponse;
import com.helloSpring.identity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPemission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);




}
