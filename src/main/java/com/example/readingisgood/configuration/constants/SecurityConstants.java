package com.example.readingisgood.configuration.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {
    public static final String DEFAULT_ROLE = "ROLE_CUSTOMER";
    public static final String BEARER = "Bearer";
    public static final String INVALID_SIGNATURE = "Invalid JWT signature: {}";
    public static final String INVALID_TOKEN = "Invalid JWT token: {}";
    public static final String TOKEN_EXPIRED = "Invalid JWT token: {}";
    public static final String TOKEN_UNSUPPORTED = "JWT token is unsupported: {}";
    public static final String EMPTY_CLAIMS = "JWT claims string is empty: {}";
}
