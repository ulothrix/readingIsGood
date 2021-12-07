//package com.example.readingisgood.utils;
//
//import com.example.readingisgood.configuration.properties.JwtProperties;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.impl.DefaultJwtParser;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.mongodb.core.FindAndModifyOptions;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mockStatic;
//
//@ExtendWith(MockitoExtension.class)
//class JwtUtilTest {
//
//    @Mock
//    private JwtProperties jwtProperties;
//    @InjectMocks
//    private JwtUtil jwtUtil;
//
//    @Test
//    void it_should_validate_token() {
//        String TOKEN = "token";
//        DefaultJwtParser defaultJwtParser = new DefaultJwtParser();
//        MockedStatic<DefaultJwtParser> staticJwtParser = mockStatic(DefaultJwtParser.class);
//        JwtParser parser = defaultJwtParser.setSigningKey(jwtProperties.getSecretKey());
//
////        staticJwtParser.when(parser.parseClaimsJws(TOKEN)).thenReturn(defaultJwtParser);
//        given(parser.parseClaimsJws(TOKEN)).willThrow(MalformedJwtException.class);
//
//        Boolean isValid = jwtUtil.validateToken(TOKEN);
//
//        assertThat(isValid).isFalse();
//    }
//
//}