package com.subject2.subscription.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;

// JWT 자체와 관련된 기능을 만드는 곳
@Slf4j
@Component
public class JwtTokenUtils {
  // JWT를 만드는 용도의 암호키
  private final Key signingKey;
  // JWT를 해석하는 용도의 객체
  private final JwtParser jwtParser; // parser: 특정한 형식의 문자열을 데이터로 다시 역직렬화하는 것

  public JwtTokenUtils(
    @Value("${jwt.secret}")
    String jwtSecret
  ) {
    log.info(jwtSecret);
    // jjwt에서 key를 활용하기 위한 준비
    this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    this.jwtParser = Jwts
      .parserBuilder()
      .setSigningKey(this.signingKey)
      .build();
  }

  // UserDetails를 받아서 JWT로 변환하는 메서드
  // UserDetails를 받아서 사용하는 이유는, Spring Security에서 UserDetails를 사용하고 있기 때문이다.
  public String generateToken(UserDetails userDetails) {
    // JWT에 담고싶은 정보를 Claims로 만든다.
    // sub: 누구인지
    // iat: 언제 발급 되었는지
    // exp: 언제 만료 예정인지
    // => 이 3가지 정보는 거의 표준으로 추가한다.

    // 현재 호출되었을 때 epoch time을 받아오는 메서드
    Instant now = Instant.now();
    Claims jwtClaims = Jwts.claims() // 일종의 Builder처럼 동작한다.
      // sub: 누구인지
      // UserDetails의 규약 자체를 따라서 만들고 있으므로
      // getUsername은 UserDetails에 무조건 있다는 것을 기대할 수 있다.
      .setSubject(userDetails.getUsername())

      // setIssuedAt
      // : 자바의 Date 클래스를 받는다.
      // 날짜를 나타내기 위한 용도

      // iat: 언제 발급 되었는지
      .setIssuedAt(Date.from(now))
      // exp: 언제 만료 예정인지
      .setExpiration(Date.from(now.plusSeconds(60 * 60 * 24 * 7))); // 일주일

      // 일반적인 JWT 외의 정보를 포함하고 싶다면
      // Map.put 사용 가능(Map을 상속 받음)
  /*
      jwtClaims.put("test", "claims");
  */

    // 최종적으로 JWT를 발급한다.
    return Jwts.builder()
      .setClaims(jwtClaims)
      .signWith(this.signingKey)
      .compact();
  }

  // 정상적인 JWT인지를 판단하는 메서드
  public boolean validate(String token) {
    try {
      // Json Web Signature
      // : Signature까지 확인하며, 정상적이지 않은 JWT라면 예외(Exception)가 발생한다.
      jwtParser.parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      log.warn("invalid jwt");
    }
    return false;
  }

  // 실제 데이터(Payload)를 반환하는 메서드
  public Claims parseClaims(String token) {
    return jwtParser
      .parseClaimsJws(token)
      .getBody();
  }
}