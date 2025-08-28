package com.helloSpring.identity_service.service;

import com.helloSpring.identity_service.dto.request.PermissionRequest;
import com.helloSpring.identity_service.dto.response.PermissionResponse;
import com.helloSpring.identity_service.entity.Permission;
import com.helloSpring.identity_service.mapper.PermissionMapper;
import com.helloSpring.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor //Tự động tạo contructor có các biến là final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) //Tự động define biến là private và final
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPemission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);

    }

    public List<PermissionResponse> getAll(){
        var permission = permissionRepository.findAll();
        return permission.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }

}
