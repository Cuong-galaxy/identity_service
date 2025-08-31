package com.helloSpring.identity_service.controller;

import com.helloSpring.identity_service.dto.request.ApiResponse;
import com.helloSpring.identity_service.dto.request.UserCreationRequest;
import com.helloSpring.identity_service.dto.request.UserUpdateRequest;
import com.helloSpring.identity_service.dto.response.UserResponse;
import com.helloSpring.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j                      // Kích hoạt logging trong class này
@RestController             // Khai báo đây là 1 controller
@RequestMapping("/users")   // Định nghĩa đường dẫn gốc cho các endpoint trong controller này
@RequiredArgsConstructor //Tự động tạo contructor có các biến là final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) //Tự động define biến là private và final
public class UserController {
    UserService userService; // Biến userService được tự động khởi tạo thông qua constructor do sử dụng @RequiredArgsConstructor

    @PostMapping
    // Định nghĩa endpoint cho phương thức HTTP POST
    ApiResponse<UserResponse>  createUser(@RequestBody @Valid UserCreationRequest request){
        // @RequestBody: Chỉ định rằng tham số request sẽ được ánh xạ từ body của yêu cầu HTTP
        // @Valid: Kích hoạt việc kiểm tra tính hợp lệ của đối tượng request dựa trên các chú thích xác thực trong lớp UserCreationRequest
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("Role: {}", grantedAuthority.getAuthority()));

        return  ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }
    // tạo api get myinfo
    @GetMapping("/me")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }


    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String  userId){
       return userService.getUser(userId);
    }


    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User has been  deleted";
    }
}
