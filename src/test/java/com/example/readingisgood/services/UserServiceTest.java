//package com.example.readingisgood.services;
//
//import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
//import com.example.readingisgood.models.responses.TokenResponse;
//import com.example.readingisgood.models.UserRegisterRequest;
//import com.example.readingisgood.persistence.repositories.UserRepository;
//import com.example.readingisgood.utils.DateUtil;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//
//import java.time.Instant;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//    @InjectMocks
//    private UserService userService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private DateUtil dateUtil;
//
//
//    @Test
//    void it_should_return_user_exists_exception(){
//        // Given
//        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
//        userRegisterRequest.setEmail("test@test.com");
//        userRegisterRequest.setPassword("123456");
//
//        String responseDate = dateUtil.getDateTimeFormatter().format(Instant.now());
//
//        UserDetails userDetails =
//
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken("user", "password");
//
//        TokenResponse tokenResponse = new TokenResponse(
//
//        );
//
//        ReadingIsGoodResponse<TokenResponse> readingIsGoodResponse = new ReadingIsGoodResponse<>();
//        // When
//        given(userService.registerUser(userRegisterRequest)).willReturn()
//
//        // Then
//    }
//}