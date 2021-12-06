package com.example.readingisgood.services;

import com.example.readingisgood.exceptions.UserAlreadyFoundException;
import com.example.readingisgood.models.requests.CustomerLoginRequest;
import com.example.readingisgood.models.requests.CustomerRegisterRequest;
import com.example.readingisgood.models.responses.MessageResponse;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.models.responses.TokenResponse;
import com.example.readingisgood.persistence.entitites.CustomerEntity;
import com.example.readingisgood.persistence.repositories.CustomerRepository;
import com.example.readingisgood.persistence.services.SequenceService;
import com.example.readingisgood.security.CustomerDetailsService;
import com.example.readingisgood.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static com.example.readingisgood.configuration.constants.SecurityConstants.DEFAULT_ROLE;
import static com.example.readingisgood.persistence.entitites.CustomerEntity.CUSTOMER_SEQUENCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private CustomerDetailsService customerDetailsService;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetails userDetails;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void it_should_register_with_granted_authority() {

        // given
        CustomerRegisterRequest registerRequest = CustomerRegisterRequest
                .builder()
                .email("test@test.com")
                .password("test123")
                .roles(List.of(DEFAULT_ROLE))
                .build();

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(sequenceService.getNextSequence(CUSTOMER_SEQUENCE));
        customerEntity.setEmail("test@test.com");
        customerEntity.setPassword(passwordEncoder.encode("test123"));
        customerEntity.setRoles(Collections.singleton(new SimpleGrantedAuthority(DEFAULT_ROLE)));

        ReadingIsGoodResponse<Void> readingIsGoodResponse = new ReadingIsGoodResponse<>();
        readingIsGoodResponse.setMessage(new MessageResponse("You've been successfully registered !"));

        given(customerRepository.existsByEmail("test@test.com")).willReturn(false);


        // when
        authenticationService.registerUser(registerRequest);

        // then
        ArgumentCaptor<CustomerEntity> customerEntityCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerRepository, times(1)).save(customerEntityCaptor.capture());
        assertThat(customerEntityCaptor.getValue().getEmail()).isEqualTo(registerRequest.getEmail());
        assertThat(customerEntityCaptor.getValue().getId()).isEqualTo(customerEntity.getId());
        assertThat(customerEntityCaptor.getValue().getRoles()).containsExactlyInAnyOrder(new SimpleGrantedAuthority(DEFAULT_ROLE));
    }

    @Test
    void it_should_register_without_granted_authority() {

        // given
        CustomerRegisterRequest registerRequest = CustomerRegisterRequest
                .builder()
                .email("test@test.com")
                .password("test123")
                .build();

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(sequenceService.getNextSequence(CUSTOMER_SEQUENCE));
        customerEntity.setEmail("test@test.com");
        customerEntity.setPassword(passwordEncoder.encode("test123"));

        ReadingIsGoodResponse<Void> readingIsGoodResponse = new ReadingIsGoodResponse<>();
        readingIsGoodResponse.setMessage(new MessageResponse("You've been successfully registered !"));

        given(customerRepository.existsByEmail("test@test.com")).willReturn(false);


        // when
        authenticationService.registerUser(registerRequest);

        // then
        ArgumentCaptor<CustomerEntity> customerEntityCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerRepository, times(1)).save(customerEntityCaptor.capture());
        assertThat(customerEntityCaptor.getValue().getEmail()).isEqualTo(registerRequest.getEmail());
        assertThat(customerEntityCaptor.getValue().getId()).isEqualTo(customerEntity.getId());
        assertThat(customerEntityCaptor.getValue().getRoles()).containsExactlyInAnyOrder(new SimpleGrantedAuthority(DEFAULT_ROLE));
    }

    @Test
    void it_should_throw_user_already_found_exception() {

        // given
        CustomerRegisterRequest registerRequest = CustomerRegisterRequest
                .builder()
                .email("test@test.com")
                .password("test123")
                .roles(List.of("CUSTOMER"))
                .build();

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(sequenceService.getNextSequence(CUSTOMER_SEQUENCE));
        customerEntity.setEmail("test@test.com");
        customerEntity.setPassword(passwordEncoder.encode("test123"));
        customerEntity.setRoles(Collections.singleton(new SimpleGrantedAuthority("CUSTOMER")));

        ReadingIsGoodResponse<Void> readingIsGoodResponse = new ReadingIsGoodResponse<>();
        readingIsGoodResponse.setMessage(new MessageResponse("You've been successfully registered !"));

        given(customerRepository.existsByEmail("test@test.com")).willReturn(true);


        // when
        UserAlreadyFoundException userAlreadyFoundException = assertThrows(UserAlreadyFoundException.class,
                () -> authenticationService.registerUser(registerRequest));

        // then
        assertThat(userAlreadyFoundException.getMessage()).isNotBlank();
    }

    @Test
    void it_should_login() {

        // given
        String TOKEN = "token";
        CustomerLoginRequest customerLoginRequest = CustomerLoginRequest
                .builder()
                .email("test@test.com")
                .password("test123")
                .build();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        "test@test.com",
                        "test123",
                        List.of(new SimpleGrantedAuthority(DEFAULT_ROLE)));

        given(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).willReturn(authentication);
        given(customerDetailsService.loadUserByUsername(customerLoginRequest.getEmail())).willReturn(userDetails);
        given(jwtUtil.generateToken(userDetails)).willReturn(TOKEN);

        // when
        ReadingIsGoodResponse<TokenResponse> readingIsGoodResponse = authenticationService.loginUser(customerLoginRequest);

        // then
        assertThat(readingIsGoodResponse.getData().getAccessToken()).isEqualTo(TOKEN);

    }
}