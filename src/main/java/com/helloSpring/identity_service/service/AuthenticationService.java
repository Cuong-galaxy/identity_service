package com.helloSpring.identity_service.service;

import com.helloSpring.identity_service.dto.request.AuthenticationRequest;
import com.helloSpring.identity_service.dto.request.IntrospectRequest;
import com.helloSpring.identity_service.dto.response.AuthenticationResponse;
import com.helloSpring.identity_service.dto.response.IntrospectResponse;
import com.helloSpring.identity_service.exception.AppException;
import com.helloSpring.identity_service.exception.ErrorCode;
import com.helloSpring.identity_service.entity.User;
import com.helloSpring.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value; // dung de su dung @Value
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
// Service này chịu trách nhiệm xác thực người dùng và tạo token
public class AuthenticationService {
    UserRepository userRepository;


    @NonFinal                       // Biến không phải final để có thể gán giá trị từ file cấu hình
    @Value("${jwt.signerKey}")      // Lấy giá trị từ file cấu hình application.properties hoặc application.yml
    protected String SIGNING_KEY;   // Khóa bí mật dùng để ký và xác thực JWT token

    // Hàm kiểm tra tính hợp lệ của token
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        // Lấy token từ yêu cầu
        var token = request.getToken();

        // Tạo bộ xác thực JWS với khóa bí mật
        JWSVerifier verifier = new MACVerifier(SIGNING_KEY.getBytes());

        // Phân tích token để lấy thông tin
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Lấy thời gian hết hạn của token
        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Xác thực token
        var verified = signedJWT.verify(verifier);

        // Trả về kết quả xác thực và thời gian hết hạn
        return IntrospectResponse.builder()
                .valid(verified && expityTime.after(new Date()))
                .build();
    }


    // Hàm xác thực người dùng và tạo token
   public AuthenticationResponse authenticate(AuthenticationRequest request){

       // Tạo bộ mã hóa mật khẩu sử dụng thuật toán BCrypt với độ phức tạp là 10
       // PswrdEncoder này dùng để so sánh mật khẩu người dùng nhập vào với mật khẩu đã mã hóa lưu trong database
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

         // Tìm người dùng trong database theo tên đăng nhập
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // So sánh mật khẩu người dùng nhập vào với mật khẩu đã mã hóa lưu trong database
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        // Nếu không xác thực được, ném ra ngoại lệ
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Nếu xác thực được, tạo token
        var token = generateToken(user);

        // Trả về token và trạng thái xác thực
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
   }


    // Hàm tạo JWT token
   private String generateToken(User user) {


         // Tạo header và payload cho JWT token
        // Header chứa thuật toán ký và loại token
       JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Payload chứa các thông tin về người dùng và thời gian hết hạn của token
       JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())                // tên đăng nhập
                .issuer("identity-service.com")             // tên dịch vụ phát hành token
                .issueTime(new Date())                      // thơờii gian phát hành token
                .expirationTime(new Date(                   // thời gian hết hạn token
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))     // các vai trò của user
                .build();

       // Tạo đối tượng Payload từ JWTClaimsSet
       Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        // Tạo đối tượng JWSObject từ header và payload
       JWSObject jwsObject = new JWSObject(header,payload);

        // Ký token với khóa bí mật
         // Trả về chuỗi token đã ký
         // Nếu có lỗi trong quá trình ký, ném ra ngoại lệ
       try {
            jwsObject.sign(new MACSigner(SIGNING_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error signing JWS object", e);
            throw new RuntimeException(e);
        }

   }

   // Hàm xây dựng chuỗi các vai trò và quyền của user
   private String buildScope(User user){
       // Tạo chuỗi các vai trò và quyền của user, ngăn cách nhau bởi dấu cách
        StringJoiner stringJoiner = new StringJoiner(" ");

        // Thêm vai trò của user vào chuỗi
       if(!CollectionUtils.isEmpty(user.getRoles()))  // kiểm tra user có vai trò không
           // Nếu có thì thêm vào chuỗi
           user.getRoles().forEach(role ->{
                   stringJoiner.add("ROLE_" + role.getName());    // Thêm tên vai trò vào chuỗi
                    // Thêm các quyền của vai trò vào chuỗi
                   if(!CollectionUtils.isEmpty(role.getPermissions()))
                       // Nếu vai trò có quyền thì thêm vào chuỗi
                        role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
           });
       // Trả về chuỗi các vai trò và quyền
       return stringJoiner.toString();
   }
}
