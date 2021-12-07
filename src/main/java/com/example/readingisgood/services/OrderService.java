package com.example.readingisgood.services;

import com.example.readingisgood.exceptions.OrderNotFoundException;
import com.example.readingisgood.exceptions.OrderNotFoundInTimeIntervalException;
import com.example.readingisgood.exceptions.StockChangeException;
import com.example.readingisgood.models.dtos.OrderBookDto;
import com.example.readingisgood.models.dtos.OrderDto;
import com.example.readingisgood.models.requests.BookRequest;
import com.example.readingisgood.models.requests.OrderRequest;
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
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Min;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.readingisgood.persistence.entitites.OrderEntity.ORDER_SEQUENCE;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final BookService bookService;
    private final SequenceService sequenceService;
    private final ModelMapper modelMapper;
    private final StatisticService statisticService;

    //todo: dto ya çevirelim.
    @Transactional
    public ReadingIsGoodResponse<OrderResponse> placeNewOrder(OrderRequest orderRequest, CustomerDetails customerDetails) {

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(sequenceService.getNextSequence(ORDER_SEQUENCE));
        orderEntity.setTotalCost(0.0);

        List<BookEntity> bookList = new ArrayList<>();

        orderRequest.getBookList().forEach(requestedBook -> {
            BookEntity bookEntity = bookService.getBook(requestedBook.getIsbn());
            if (bookEntity.getStock() > requestedBook.getStock()) {
                bookEntity.setStock(bookEntity.getStock() - requestedBook.getStock());
            }
            if (bookService.getBook(requestedBook.getIsbn()).getVersion() == bookEntity.getVersion()) {
                bookService.updateStock(new BookRequest(bookEntity.getIsbn(), bookEntity.getStock()));
                bookList.add(bookEntity);
                orderEntity.setTotalCost(orderEntity.getTotalCost() + bookEntity.getPrice() * requestedBook.getStock());
                orderEntity.setPurchasedBookCount(orderEntity.getPurchasedBookCount() + requestedBook.getStock());
            } else {
                throw new StockChangeException(bookEntity.getIsbn());
            }
        });

        orderEntity.setBooks(bookList);
        // todo: aşağıda ki date i hatalı girerek istek atalım. Eklememesi gereken entryi de mi ekliyor ?
        LocalDateTime localDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime();
        orderEntity.setOrderPlacedDate(localDateTime);
        orderRepository.save(orderEntity);

        CustomerEntity customerEntity = customerRepository.findCustomerEntityByEmail(customerDetails.getEmail());
        if (Collections.isEmpty(customerEntity.getOrders())) {
            customerEntity.setOrders(new ArrayList<>());
        }
        customerEntity.getOrders().add(orderEntity);

        statisticService.updateStatisticsForCustomer(customerDetails, orderEntity);

        customerRepository.save(customerEntity);

        OrderResponse orderResponse = OrderResponse.builder()
                .orders(new ArrayList<>())
                .build();

        return constructOrdersToDto(List.of(orderEntity), orderResponse);
    }

    public ReadingIsGoodResponse<OrderDto> getOrderDetail(@Min(0) Long id, UserDetails userDetails) {
        CustomerEntity customerEntity = customerRepository.findCustomerEntityByEmail(userDetails.getUsername());

        List<OrderEntity> orderEntityList = Optional.ofNullable(customerEntity.getOrders()).orElseThrow(OrderNotFoundException::new);
        List<OrderEntity> orderEntities = orderEntityList.stream()
                .filter(order -> Objects.equals(order.getId(), id)).collect(Collectors.toList());

        if (!orderEntities.isEmpty()) {
            OrderDto orderDto = OrderDto.builder()
                    .id(orderEntities.get(0).getId())
                    .bookList(convertType(orderEntities.get(0).getBooks()))
                    .build();
            return new ReadingIsGoodResponse<>(orderDto);
        } else {
            throw new OrderNotFoundException();
        }
    }

    public ReadingIsGoodResponse<OrderResponse> getOrdersByTimeInterval(UserDetails userDetails, OrdersTimeIntervalRequest ordersTimeIntervalRequest) {
        CustomerEntity customerEntity = customerRepository.findCustomerEntityByEmail(userDetails.getUsername());

        // todo: buradaki date işlemleri düzenlenmeli.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(ordersTimeIntervalRequest.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(ordersTimeIntervalRequest.getEndDate(), formatter);

        List<OrderEntity> orderEntities = customerEntity.getOrders()
                .stream()
                .filter(d -> d.getOrderPlacedDate().isAfter(startDate) && d.getOrderPlacedDate().isBefore(endDate))
                .collect(Collectors.toList());


        if (!orderEntities.isEmpty()) {
            OrderResponse orderResponse = OrderResponse
                    .builder()
                    .orders(new ArrayList<>())
                    .build();

            return constructOrdersToDto(orderEntities, orderResponse);
        } else {
            throw new OrderNotFoundInTimeIntervalException();
        }

    }

    public ReadingIsGoodResponse<OrderResponse> constructOrdersToDto(List<OrderEntity> orderEntityList, OrderResponse orderResponse) {

        ReadingIsGoodResponse<OrderResponse> response = new ReadingIsGoodResponse<>();
        orderEntityList.forEach(order -> {
            Type listType = new TypeToken<List<OrderBookDto>>() {
            }.getType();
            List<OrderBookDto> bookDtoList = modelMapper.map(order.getBooks(), listType);
            OrderDto orderDto = OrderDto.builder()
                    .id(order.getId())
                    .bookList(bookDtoList)
                    .build();
            orderResponse.getOrders().add(orderDto);
        });
        response.setData(orderResponse);
        log.info("Order placed: {}", orderResponse);
        return response;
    }

    private List<OrderBookDto> convertType(List<BookEntity> bookEntityList) {
        Type listType = new TypeToken<List<OrderBookDto>>() {
        }.getType();
        return modelMapper.map(bookEntityList, listType);
    }
}
