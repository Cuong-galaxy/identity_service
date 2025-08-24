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
public class AuthenticationService {
    UserRepository userRepository;


    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNING_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNING_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expityTime.after(new Date()))
                .build();
    }


   public AuthenticationResponse authenticate(AuthenticationRequest request){
       PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
       var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
   }

   private String generateToken(User user) {
       // logic to generate header and payload for JWT
       // Example of creating a JWSHeader with HS512 algorithm
       JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
       JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // ten nguoi dung
                .issuer("identity-service.com")
                .issueTime(new Date())   // thoi gian tao token
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user)) // các vai trò của user
                .build();
       Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        // Logic to generate JWT token
       JWSObject jwsObject = new JWSObject(header,payload);

       try {
            jwsObject.sign(new MACSigner(SIGNING_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error signing JWS object", e);
            throw new RuntimeException(e);
        }

   }
   private String buildScope(User user){
       StringJoiner stringJoiner = new StringJoiner(" ");
       if(!CollectionUtils.isEmpty(user.getRoles()))
           user.getRoles().forEach(stringJoiner::add);
       return stringJoiner.toString();
   }
}
