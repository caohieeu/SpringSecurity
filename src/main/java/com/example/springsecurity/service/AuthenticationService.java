package com.example.springsecurity.service;

import com.example.springsecurity.dto.request.AuthenticateRequest;
import com.example.springsecurity.dto.request.IntrospectRequest;
import com.example.springsecurity.dto.request.RegisterRequest;
import com.example.springsecurity.dto.response.ApiResponse;
import com.example.springsecurity.dto.response.AuthenticateResponse;
import com.example.springsecurity.dto.response.IntrospectResponse;
import com.example.springsecurity.dto.response.UserResponse;
import com.example.springsecurity.enums.Role;
import com.example.springsecurity.exception.AppException;
import com.example.springsecurity.exception.ErrorCode;
import com.example.springsecurity.mapper.UserMapper;
import com.example.springsecurity.model.Users;
import com.example.springsecurity.repository.UsersRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UsersRepository usersRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGN_KEY;
    public UserResponse register(RegisterRequest request) {
        var chkUser = usersRepository.findByUsername(request.getUsername())
                .orElse(null);
        if(chkUser == null) {
            Users user = userMapper.toUser(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            HashSet<String> roles = new HashSet<>();
            roles.add(Role.USER.name());

            user.setRoles(roles);

            return userMapper.toUserResponse(
                    usersRepository.save(user)
            );
        }
        throw new AppException(ErrorCode.USER_EXISTEED);
    }

    public AuthenticateResponse authenticate(AuthenticateRequest request) throws JOSEException {
        var user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean chkPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(chkPassword) {
            String token = generateToken(user);

            return AuthenticateResponse.builder()
                    .authenticated(chkPassword)
                    .token(token)
                    .build();
        }
        else {
            throw  new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private String generateToken(Users user) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("caohieeu.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .claim("user_id", user.getId())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));

        return jwsObject.serialize();
    }
    private String buildScope(Users user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
    public boolean checkToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return signedJWT.verify(verifier) && expiryTime.after(new Date());
    }
    public IntrospectResponse introspectToken(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        return IntrospectResponse.builder()
                .valid(checkToken(token))
                .build();
    }
}
