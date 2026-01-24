package com.buyukozkan.rotation.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
