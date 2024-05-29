package com.example.springsecurity.service;

import com.example.springsecurity.dto.request.AuthenticateRequest;
import com.example.springsecurity.dto.request.IntrospectRequest;
import com.example.springsecurity.dto.request.RegisterRequest;
import com.example.springsecurity.dto.response.AuthenticateResponse;
import com.example.springsecurity.dto.response.IntrospectResponse;
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

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UsersRepository usersRepository;
    UserMapper userMapper;
    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGN_KEY;
    public Users register(RegisterRequest request) {
        var chkUser = usersRepository.findByUsername(request.getUsername())
                .orElse(null);
        if(chkUser == null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            request.setPassword(passwordEncoder.encode(request.getPassword()));

            return usersRepository.save(
                    userMapper.toUser(request)
            );
        }
        throw new AppException(ErrorCode.USER_EXISTEED);
    }

    public AuthenticateResponse authenticate(AuthenticateRequest request) throws JOSEException {
        var user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean chkPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());

        String token = generateToken(request.getUsername());

        return AuthenticateResponse.builder()
                .authenticated(chkPassword)
                .token(token)
                .build();
    }

    private String generateToken(String username) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("caohieeu.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("CustomClaim", "MyClaim")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jweObject = new JWSObject(header, payload);

        jweObject.sign(new MACSigner(SIGN_KEY.getBytes()));
        return jweObject.serialize();
    }

    public IntrospectResponse introspectToken(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier) && expiryTime.after(new Date());

        return IntrospectResponse.builder()
                .valid(verified)
                .build();
    }
}
