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


    // Phươngthức tạo permission
    public PermissionResponse create(PermissionRequest request){   //Đầu vào là PermissionRequest, đầu ra là PermissionResponse

        // Tao permission từ request
        Permission permission = permissionMapper.toPemission(request);

        // Lưu permission vào database
        permission = permissionRepository.save(permission);

        // Trả về permissionResponse
        return permissionMapper.toPermissionResponse(permission);

    }

    // Phương thức lấy tất cả permission
    public List<PermissionResponse> getAll(){

        // Lấy tất cả permission từ database
        var permission = permissionRepository.findAll();

        // Chuyển đổi danh sách permission thành danh sách permissionResponse và trả về
        return permission.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }

}
