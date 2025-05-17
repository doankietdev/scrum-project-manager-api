package com.doankietdev.identityservice.infrastructure.security.impl;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doankietdev.identityservice.application.exception.AppException;
import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.infrastructure.config.AuthProperties;
import com.doankietdev.identityservice.infrastructure.model.AuthUser;
import com.doankietdev.identityservice.infrastructure.model.KeyToken;
import com.doankietdev.identityservice.infrastructure.model.TokenPayload;
import com.doankietdev.identityservice.infrastructure.security.KeyTokenInfrasService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class KeyTokenInfrasServiceImpl implements KeyTokenInfrasService {
  @Autowired
  AuthProperties authProperties;

  @Override
  public KeyToken createKeyToken(AuthUser authUser) {
    KeyPair keyPair;
    try {
      keyPair = createKeyPair();
    } catch (NoSuchAlgorithmException e) {
      throw AppException.builder().appCode(AppCode.SERVER_ERROR).build();
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
      jwsSigner = getSigner(privateKey);
      signedAccessJWT.sign(jwsSigner);
      signedRefreshJWT.sign(jwsSigner);
    } catch (JOSEException e) {
      throw AppException.builder().appCode(AppCode.SERVER_ERROR).build();
    }

    return KeyToken.builder()
        .accessToken(signedAccessJWT.serialize())
        .refreshToken(signedRefreshJWT.serialize())
        .publicKey(publicKey.getEncoded())
        .jti(jti)
        .expiresAt(refreshJWTClaimsSet.getExpirationTime().toInstant())
        .build();
  }

  @Override
  public TokenPayload verifyToken(String token, byte[] publicKey) {
    RSAPublicKey rsaPublicKey;
    try {
      rsaPublicKey = convertToRSAPublicKey(publicKey);
    } catch (Exception e) {
      throw AppException.builder().appCode(AppCode.SERVER_ERROR).build();
    }

    SignedJWT signedJWT;
    try {
      signedJWT = SignedJWT.parse(token);
    } catch (ParseException e) {
      throw AppException.builder().appCode(AppCode.TOKEN_INVALID).build();
    }
    boolean isValidSignature;
    try {
      isValidSignature = signedJWT.verify(getVerifier(rsaPublicKey));
    } catch (JOSEException e) {
      throw AppException.builder().appCode(AppCode.TOKEN_INVALID).build();
    }
    if (!isValidSignature)
      throw AppException.builder().appCode(AppCode.TOKEN_INVALID).build();

    JWTClaimsSet jwtClaimsSet;
    try {
      jwtClaimsSet = signedJWT.getJWTClaimsSet();
    } catch (ParseException e) {
      throw AppException.builder().appCode(AppCode.SERVER_ERROR).build();
    }
    Instant expiresAt = jwtClaimsSet.getExpirationTime().toInstant();
    Instant now = Instant.now();
    boolean isExpiredToken = expiresAt.isBefore(now) || expiresAt.equals(now);
    if (isExpiredToken)
      throw AppException.builder().appCode(AppCode.ACCESS_TOKEN_EXPIRED).build();

    return TokenPayload.builder()
        .userId(jwtClaimsSet.getSubject())
        .jti(jwtClaimsSet.getJWTID())
        .build();
  }

  @Override
  public TokenPayload parseToken(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
      return TokenPayload.builder()
          .userId(jwtClaimsSet.getSubject())
          .jti(jwtClaimsSet.getJWTID())
          .build();
    } catch (ParseException e) {
      throw AppException.builder().appCode(AppCode.TOKEN_INVALID).build();
    }
  }

  private KeyPair createKeyPair() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    return keyPairGenerator.genKeyPair();
  }

  public RSAPublicKey convertToRSAPublicKey(byte[] publicKeyBytes) throws Exception {
    X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(spec);
    return (RSAPublicKey) publicKey;
  }

  private JWSSigner getSigner(RSAPrivateKey privateKey) throws JOSEException {
    return new RSASSASigner(privateKey);
  }

  private JWSVerifier getVerifier(RSAPublicKey publicKey) throws JOSEException {
    return (new RSASSAVerifier(publicKey));
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
