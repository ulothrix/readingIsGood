package com.example.readingisgood.services;

import com.example.readingisgood.exceptions.OrderNotFoundException;
import com.example.readingisgood.models.dtos.BookDto;
import com.example.readingisgood.models.dtos.OrderBookDto;
import com.example.readingisgood.models.dtos.OrderDto;
import com.example.readingisgood.models.requests.OrdersTimeIntervalRequest;
import com.example.readingisgood.models.responses.OrderResponse;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.persistence.entitites.BookEntity;
import com.example.readingisgood.persistence.entitites.CustomerEntity;
import com.example.readingisgood.persistence.entitites.OrderEntity;
import com.example.readingisgood.persistence.repositories.CustomerRepository;
import com.example.readingisgood.persistence.repositories.OrderRepository;
import com.example.readingisgood.persistence.services.SequenceService;
import com.example.readingisgood.security.CustomerDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.readingisgood.configuration.constants.SecurityConstants.DEFAULT_ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BookService bookService;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private StatisticService statisticService;
    @InjectMocks
    private OrderService orderService;

    @Test
    void it_should_get_order_details() {
        // given
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(10L);
        bookEntity.setName("book1");
        bookEntity.setAuthor("author1");
        bookEntity.setIsbn("1111111111111");
        bookEntity.setPrice(10.0);
        bookEntity.setStock(100);


        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(5L);
        orderEntity.setBooks(List.of(bookEntity));
        orderEntity.setTotalCost(20.0);
        orderEntity.setOrderPlacedDate(LocalDateTime.of(2021, Month.DECEMBER, 6, 22, 22));
        orderEntity.setPurchasedBookCount(2);

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setEmail("test@test.com");
        customerEntity.setPassword("123456");
        customerEntity.setOrders(List.of(orderEntity));
        customerEntity.setRoles(Set.of(new SimpleGrantedAuthority(DEFAULT_ROLE)));

        CustomerDetails customerDetails = CustomerDetails.getUserDetails(customerEntity);

        OrderBookDto orderBookDto = OrderBookDto
                .builder()
                .id(10L)
                .isbn("1111111111111")
                .build();

        Type listType = new TypeToken<List<OrderBookDto>>() {
        }.getType();

        given(customerRepository.findCustomerEntityByEmail(customerDetails.getEmail())).willReturn(customerEntity);
        given(modelMapper.map(List.of(bookEntity), listType)).willReturn(List.of(orderBookDto));

        // when
        ReadingIsGoodResponse<OrderDto> response = orderService.getOrderDetail(orderEntity.getId(), customerDetails);

        // then
        assertThat(response.getData().getId()).isEqualTo(5L);
        assertThat(response.getData().getBookList()).hasSize(1);
        assertThat(response.getData().getBookList().get(0).getIsbn()).isEqualTo("1111111111111");
        assertThat(response.getData().getBookList().get(0).getId()).isEqualTo(10L);
    }

    @Test
    void it_should_throw_order_not_found_exception() {
        // given
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setEmail("test@test.com");
        customerEntity.setPassword("123456");
        customerEntity.setOrders(new ArrayList<>());
        customerEntity.setRoles(Set.of(new SimpleGrantedAuthority(DEFAULT_ROLE)));

        CustomerDetails customerDetails = CustomerDetails.getUserDetails(customerEntity);

        given(customerRepository.findCustomerEntityByEmail(customerDetails.getEmail())).willReturn(customerEntity);

        // when
        OrderNotFoundException orderNotFoundException = assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrderDetail(-1L, customerDetails));

        // then
        assertThat(orderNotFoundException.getMessage()).isNotBlank();
    }

    @Test
    void it_should_return_orders_in_time_intervals() {
        // given

        OrderBookDto orderBookDto = OrderBookDto
                .builder()
                .id(10L)
                .isbn("1111111111111")
                .name("book1")
                .author("author1")
                .build();

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(10L);
        bookEntity.setName("book1");
        bookEntity.setAuthor("author1");
        bookEntity.setIsbn("1111111111111");
        bookEntity.setPrice(10.0);
        bookEntity.setStock(100);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(5L);
        orderEntity.setBooks(List.of(bookEntity));
        orderEntity.setTotalCost(20.0);
        orderEntity.setOrderPlacedDate(LocalDateTime.of(2021, Month.DECEMBER, 7, 22, 22));
        orderEntity.setPurchasedBookCount(2);

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setEmail("test@test.com");
        customerEntity.setPassword("123456");
        customerEntity.setOrders(List.of(orderEntity));
        customerEntity.setRoles(Set.of(new SimpleGrantedAuthority(DEFAULT_ROLE)));

        CustomerDetails customerDetails = CustomerDetails.getUserDetails(customerEntity);

        OrdersTimeIntervalRequest ordersTimeIntervalRequest = OrdersTimeIntervalRequest
                .builder()
                .startDate("2021-12-06 22:22")
                .endDate("2021-12-08 22:22")
                .build();


        Type listType = new TypeToken<List<OrderBookDto>>() {
        }.getType();

        given(customerRepository.findCustomerEntityByEmail(customerDetails.getUsername())).willReturn(customerEntity);
        given(modelMapper.map(orderEntity.getBooks(), listType)).willReturn(List.of(orderBookDto));
        // when
        ReadingIsGoodResponse<OrderResponse> response = orderService.getOrdersByTimeInterval(customerDetails,ordersTimeIntervalRequest);

        // then
        assertThat(response.getData().getOrders()).hasSize(1);
        assertThat(response.getData().getOrders().get(0).getBookList().get(0).getName()).isEqualTo("book1");
        assertThat(response.getData().getOrders().get(0).getBookList().get(0).getIsbn()).isEqualTo("1111111111111");

    }
}