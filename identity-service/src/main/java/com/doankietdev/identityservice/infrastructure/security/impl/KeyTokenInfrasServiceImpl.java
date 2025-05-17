package com.doankietdev.identityservice.infrastructure.security.impl;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doankietdev.identityservice.application.model.dto.AuthKeyToken;
import com.doankietdev.identityservice.application.model.dto.AuthUser;
import com.doankietdev.identityservice.infrastructure.config.AuthProperties;
import com.doankietdev.identityservice.infrastructure.security.KeyTokenInfrasService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class KeyTokenInfrasServiceImpl implements KeyTokenInfrasService {
  @Autowired
  AuthProperties authProperties;

  @Override
  public AuthKeyToken createKeyToken(AuthUser authUser) {
    KeyPair keyPair;
    try {
      keyPair = createKeyPair();
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

    String jti = UUID.randomUUID().toString();

    JWTClaimsSet refreshJWTClaimsSet = getJWTClaimsSet(authUser, jti, authProperties.getRefreshTokenTime());
    SignedJWT signedAccessJWT = new SignedJWT(getJWSHeader(),
        getJWTClaimsSet(authUser, jti, authProperties.getAccessTokenTime()));
    SignedJWT signedRefreshJWT = new SignedJWT(getJWSHeader(), refreshJWTClaimsSet);

    JWSSigner jwsSigner;
    try {
      jwsSigner = getRSASigner(privateKey);
      signedAccessJWT.sign(jwsSigner);
      signedRefreshJWT.sign(jwsSigner);
    } catch (JOSEException e) {
      return null;
    }

    return AuthKeyToken.builder()
        .accessToken(signedAccessJWT.serialize())
        .refreshToken(signedRefreshJWT.serialize())
        .publicKey(publicKey.getEncoded())
        .jti(jti)
        .expiresAt(refreshJWTClaimsSet.getExpirationTime().toInstant())
        .build();
  }

  private KeyPair createKeyPair() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    return keyPairGenerator.genKeyPair();
  }

  private JWSSigner getRSASigner(RSAPrivateKey privateKey) throws JOSEException {
    return new RSASSASigner(privateKey);
  }

  private JWSHeader getJWSHeader() {
    return new JWSHeader(JWSAlgorithm.RS256);
  }

  private JWTClaimsSet getJWTClaimsSet(AuthUser authUser, String jti, long expirationTime) {
    Instant now = Instant.now();
    return new JWTClaimsSet.Builder()
        .subject(authUser.getUserId())
        .issuer("http://localhost:8080")
        .issueTime(Date.from(now))
        .expirationTime(Date.from(now.plusSeconds(expirationTime)))
        .jwtID(jti)
        .build();
  }
}
