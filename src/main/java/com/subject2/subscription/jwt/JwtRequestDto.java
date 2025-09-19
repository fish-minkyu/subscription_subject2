package com.subject2.subscription.jwt;

import lombok.Data;

@Data
public class JwtRequestDto {
  private String username;
  private String password;
}