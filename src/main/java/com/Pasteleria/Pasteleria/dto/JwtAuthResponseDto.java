package com.Pasteleria.Pasteleria.dto;

import lombok.Data;

@Data
public class JwtAuthResponseDto {

    private String accessToken;
    private String tokenType = "Bearer";

    private String nombre;
    private String email;
    private String rol;

    public JwtAuthResponseDto() {}
}
