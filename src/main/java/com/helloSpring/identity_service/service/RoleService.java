package com.helloSpring.identity_service.service;

import com.helloSpring.identity_service.dto.request.PermissionRequest;
import com.helloSpring.identity_service.dto.request.RoleRequest;
import com.helloSpring.identity_service.dto.response.PermissionResponse;
import com.helloSpring.identity_service.dto.response.RoleResponse;
import com.helloSpring.identity_service.entity.Permission;
import com.helloSpring.identity_service.entity.Role;
import com.helloSpring.identity_service.mapper.PermissionMapper;
import com.helloSpring.identity_service.mapper.RoleMapper;
import com.helloSpring.identity_service.repository.PermissionRepository;
import com.helloSpring.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor //Tự động tạo contructor có các biến là final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) //Tự động define biến là private và final
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);

        var permission = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permission));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);

    }

    public List<RoleResponse> getAll(){
        var role = roleRepository.findAll();
        return role
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }

}
