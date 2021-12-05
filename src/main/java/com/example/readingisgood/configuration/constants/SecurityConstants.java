package com.example.readingisgood.configuration.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {
    public static final String DEFAULT_ROLE = "ROLE_CUSTOMER";
    public static final String BEARER = "Bearer";
    public static final String INVALID_SIGNATURE_LOG = "Invalid JWT signature: {}";
    public static final String INVALID_TOKEN_LOG = "Invalid JWT token: {}";
    public static final String TOKEN_EXPIRED_LOG = "Invalid JWT token: {}";
    public static final String TOKEN_UNSUPPORTED_LOG = "JWT token is unsupported: {}";
    public static final String EMPTY_CLAIMS_LOG = "JWT claims string is empty: {}";
}
