//package com.example.readingisgood.services;
//
//import com.example.readingisgood.models.responses.OrderResponse;
//import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
//import com.example.readingisgood.persistence.entitites.BookEntity;
//import com.example.readingisgood.persistence.entitites.CustomerEntity;
//import com.example.readingisgood.persistence.entitites.OrderEntity;
//import com.example.readingisgood.persistence.repositories.CustomerRepository;
//import com.example.readingisgood.security.CustomerDetails;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import static com.example.readingisgood.configuration.constants.SecurityConstants.DEFAULT_ROLE;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doReturn;
//
//@ExtendWith(MockitoExtension.class)
//class CustomerServiceTest {
//
//    @Mock
//    private CustomerRepository customerRepository;
//    @Mock
//    private OrderService orderService;
//    @Spy
//    private ModelMapper modelMapper;
//    @InjectMocks
//    private CustomerService customerService;
//
//    @Test
//    void it_should_get_orders_of_given_customer() {
//        // given
//        BookEntity bookEntity = new BookEntity();
//        bookEntity.setId(1L);
//        bookEntity.setIsbn("1111111111111");
//        bookEntity.setPrice(10.0);
//        bookEntity.setStock(50);
//
//        BookEntity bookEntity2 = new BookEntity();
//        bookEntity2.setId(2L);
//        bookEntity2.setIsbn("2222222222222");
//        bookEntity2.setPrice(20.0);
//        bookEntity2.setStock(50);
//
//        OrderEntity orderEntity = new OrderEntity();
//        orderEntity.setId(22L);
//        orderEntity.setTotalCost(60.0);
//        orderEntity.setOrderPlacedDate(LocalDateTime.of(2021, Month.DECEMBER, 7, 22, 22));
//        orderEntity.setPurchasedBookCount(4);
//        orderEntity.setBooks(List.of(bookEntity, bookEntity2));
//
//
//        CustomerEntity customerEntity = new CustomerEntity();
//        customerEntity.setId(7L);
//        customerEntity.setEmail("test@test.com");
//        customerEntity.setPassword("123456");
//        customerEntity.setRoles(Set.of(new SimpleGrantedAuthority(DEFAULT_ROLE)));
//        customerEntity.setOrders(List.of(orderEntity));
//
//        CustomerDetails customerDetails = CustomerDetails.getUserDetails(customerEntity);
//
//        Optional<CustomerEntity> customerEntityOptional = Optional.of(customerEntity);
//
//        OrderResponse orderResponse = OrderResponse
//                .builder()
//                .orders(new ArrayList<>())
//                .build();
//
//        ReadingIsGoodResponse<OrderResponse> response = new ReadingIsGoodResponse<>();
//
//
//        given(customerRepository.findByEmail(customerDetails.getEmail())).willReturn(customerEntityOptional);
//        given(orderService.constructOrdersToDto(List.of(orderEntity), orderResponse)).willReturn(response);
//        // when
//        response = customerService.getOrders(customerDetails);
//
//        // then
//        assertThat(response.getData().getOrders()).hasSize(1);
//
//
//    }
//
//}