# Identity-service# Identity-service - Spring Boot

## Giới thiệu

Dự án này được xây dựng nhằm mục đích học tập và thực hành Java Spring Boot. Ứng dụng mô phỏng hệ thống quản lý người dùng, vai trò và phân quyền, cung cấp các API xác thực, quản lý người dùng, vai trò và quyền hạn. Phù hợp cho các hệ thống cần kiểm soát truy cập và bảo mật.

## Công nghệ sử dụng
- Java 11
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- JWT (JSON Web Tokens)
- Lombok
- Docker (tùy chọn)
- Postman (tùy chọn)
## Các package và file cấu hình quan trọng

- `configuration/`  
  Chứa các file cấu hình bảo mật, khởi tạo dữ liệu và xử lý xác thực JWT:  
  - `SecurityConfig.java`: Cấu hình bảo mật Spring Security.  
  - `ApplicationInitConfig.java`: Khởi tạo dữ liệu mặc định khi ứng dụng chạy.  
  - `jwtAuthenticationEntryPoint.java`: Xử lý lỗi xác thực JWT.

- `controller/`  
  Định nghĩa các API cho xác thực, quản lý người dùng, vai trò và quyền.

- `entity/`  
  Khai báo các thực thể JPA như User, Role, Permission, ánh xạ với bảng dữ liệu.

- `repository/`  
  Các interface truy xuất dữ liệu từ database cho User, Role, Permission.

- `service/`  
  Chứa logic nghiệp vụ cho xác thực, quản lý người dùng, vai trò và quyền.

- `exception/`  
  Xử lý ngoại lệ và mã lỗi toàn cục.

- `resources/application.yaml`  