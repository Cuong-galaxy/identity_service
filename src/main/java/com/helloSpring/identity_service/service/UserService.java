package com.helloSpring.identity_service.service;


import com.helloSpring.identity_service.dto.request.UserCreationRequest;
import com.helloSpring.identity_service.dto.request.UserUpdateRequest;
import com.helloSpring.identity_service.dto.response.UserResponse;
import com.helloSpring.identity_service.entity.User;
import com.helloSpring.identity_service.enums.Role;
import com.helloSpring.identity_service.exception.AppException;
import com.helloSpring.identity_service.exception.ErrorCode;
import com.helloSpring.identity_service.mapper.UserMapper;
import com.helloSpring.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor //Tự động tạo contructor có các biến là final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) //Tự động define biến là private và final
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request){
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);


        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        //user.setRoles(roles);

        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    // Sự khác nhau giữa @PreAuthorize và @PostAuthorize
    // @PreAuthorize: Kiểm tra quyền trước khi phương thức được gọi
    // @PostAuthorize: Kiểm tra quyền sau khi phương thức được gọi
    // Ví dụ: @PreAuthorize("hasRole('ADMIN')") chỉ cho phép người dùng có vai trò ADMIN gọi phương thức
    // Ví dụ: @PostAuthorize("returnObject.owner == authentication.name") chỉ cho phép người dùng gọi phương thức nếu đối tượng trả về có thuộc tính owner trùng với tên người dùng đã xác thực


    //Chỉ có ADMIN mới được phép gọi api này
    //Trước khi gọi api này sẽ kiểm tra role của user trong token có phải là ADMIN không
    //Nếu không phải sẽ trả về lỗi 403
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(){
        log.info("In method get users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    //Chỉ có ADMIN hoặc chính user đó mới được phép gọi api này
    //Nếu không phải sẽ trả về lỗi 403
    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }


    //Luyện tập tạo Api get myinfo, so sánh username trong token với username trong db, nếu trùng thì trả về thông tin user
    public UserResponse getMyInfo(){
        //Lấy username từ token
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        //Tìm user trong db
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);

    }



    public  UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));

    }
    public void deleteUser(String userId ){
        userRepository.deleteById(userId);
    }

}
