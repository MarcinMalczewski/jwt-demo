package com.example.jwtdemo;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Log4j2
@Service
public class JwtToken {

    @Value("${spring.security.oauth2.resourceserver.jwt.private-key}")
    RSAPrivateKey rsaPrivateKey;

    @Value("${spring.security.oauth2.resourceserver.jwt.public-key}")
    RSAPublicKey rsaPublicKey;

    public String generate(String username) throws JOSEException {
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(3, ChronoUnit.MINUTES);

        // Convert to JWK format
        RSAKey privateKey = new RSAKey.Builder(rsaPublicKey)
                .privateKey(rsaPrivateKey).build();

        // Create JWT
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(privateKey.getKeyID()).build(),
                new JWTClaimsSet.Builder()
                        .subject(username)
                        .claim("email", "user.name@example.com")
                        .claim("userId", "123-456")
                        .claim("scope", ("ADMIN".equalsIgnoreCase(username) ? "message:read" : null))
                        .issueTime(Date.from(issuedAt))
                        .expirationTime(Date.from(expiration))
                        .issuer("https://jwt-demo.example.com")
                        .build());

        // Sign the JWT
        signedJWT.sign(new RSASSASigner(privateKey));
        var jwtStr = signedJWT.serialize();
        log.info("signed jwt: " + jwtStr);

        return jwtStr;
    }
}
