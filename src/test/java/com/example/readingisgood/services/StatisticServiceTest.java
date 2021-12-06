package com.example.readingisgood.services;

import com.example.readingisgood.exceptions.StatisticsNotFoundException;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.persistence.entitites.CustomerEntity;
import com.example.readingisgood.persistence.entitites.OrderEntity;
import com.example.readingisgood.persistence.entitites.StatisticEntity;
import com.example.readingisgood.persistence.repositories.StatisticRepository;
import com.example.readingisgood.persistence.services.SequenceService;
import com.example.readingisgood.security.CustomerDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static com.example.readingisgood.configuration.constants.SecurityConstants.DEFAULT_ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {

    @Mock
    private SequenceService sequenceService;
    @Mock
    private StatisticRepository statisticRepository;
    @InjectMocks
    private StatisticService statisticService;

    @Test
    void it_should_get_statistics_of_given_customer() {
        // given

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(7L);
        customerEntity.setEmail("test@test.com");
        customerEntity.setPassword("123456");
        customerEntity.setRoles(Set.of(new SimpleGrantedAuthority(DEFAULT_ROLE)));

        CustomerDetails customerDetails = CustomerDetails.getUserDetails(customerEntity);

        String EMAIL = "test@test.com";
        StatisticEntity statisticEntity = new StatisticEntity();
        statisticEntity.setId(1L);
        statisticEntity.setEmail(EMAIL);
        statisticEntity.setMonth("Dec");
        statisticEntity.setTotalPurchasedAmount(150.50);
        statisticEntity.setTotalBookCount(50);
        statisticEntity.setTotalOrderCount(7);

        StatisticEntity statisticEntity2 = new StatisticEntity();
        statisticEntity2.setId(2L);
        statisticEntity2.setEmail(EMAIL);
        statisticEntity2.setMonth("Jan");
        statisticEntity2.setTotalPurchasedAmount(70.70);
        statisticEntity2.setTotalBookCount(23);
        statisticEntity2.setTotalOrderCount(3);

        List<StatisticEntity> statisticEntities = List.of(statisticEntity, statisticEntity2);

        given(statisticRepository.getStatisticEntityByEmail(customerDetails.getEmail())).willReturn(statisticEntities);

        // when
        ReadingIsGoodResponse<List<StatisticEntity>> response = statisticService.getStatistics(customerDetails);

        // then
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(statisticRepository, times(1)).getStatisticEntityByEmail(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo("test@test.com");
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData()).containsExactlyInAnyOrder(statisticEntity, statisticEntity2);

    }

    @Test
    void it_should_throw_statistics_not_found_exception() {
        // given

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(7L);
        customerEntity.setEmail("test@test.com");
        customerEntity.setPassword("123456");
        customerEntity.setRoles(Set.of(new SimpleGrantedAuthority(DEFAULT_ROLE)));

        CustomerDetails customerDetails = CustomerDetails.getUserDetails(customerEntity);

        List<StatisticEntity> statisticEntities = List.of();

        given(statisticRepository.getStatisticEntityByEmail(customerDetails.getEmail())).willReturn(statisticEntities);

        // when
        StatisticsNotFoundException statisticsNotFoundException = assertThrows(StatisticsNotFoundException.class,
                () -> statisticService.getStatistics(customerDetails));

        // then
        assertThat(statisticsNotFoundException.getMessage()).isNotBlank();
    }

    @Test
    void it_should_update_first_entry_of_month() {
        // given
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(7L);
        customerEntity.setEmail("test@test.com");
        customerEntity.setPassword("123456");
        customerEntity.setRoles(Set.of(new SimpleGrantedAuthority(DEFAULT_ROLE)));

        CustomerDetails customerDetails = CustomerDetails.getUserDetails(customerEntity);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setOrderPlacedDate(LocalDateTime.of(2021, Month.DECEMBER, 6, 22, 22));
        orderEntity.setPurchasedBookCount(3);
        orderEntity.setTotalCost(150.0);

        String EMAIL = "test@test.com";
        StatisticEntity statisticEntity = new StatisticEntity();
        statisticEntity.setId(1L);
        statisticEntity.setEmail(EMAIL);
        statisticEntity.setMonth("Oct");
        statisticEntity.setTotalPurchasedAmount(150.50);
        statisticEntity.setTotalBookCount(50);
        statisticEntity.setTotalOrderCount(7);

        List<StatisticEntity> usersAllStatistics = List.of(statisticEntity);
        given(statisticRepository.getStatisticEntityByEmail(customerDetails.getEmail())).willReturn(usersAllStatistics);

        // when
        statisticService.updateStatisticsForCustomer(customerDetails, orderEntity);

        // then
        ArgumentCaptor<StatisticEntity> statisticEntityArgumentCaptor = ArgumentCaptor.forClass(StatisticEntity.class);
        verify(statisticRepository, times(1)).save(statisticEntityArgumentCaptor.capture());
        assertThat(statisticEntityArgumentCaptor.getValue().getTotalBookCount()).isEqualTo(orderEntity.getPurchasedBookCount());
        assertThat(statisticEntityArgumentCaptor.getValue().getTotalPurchasedAmount()).isEqualTo(orderEntity.getTotalCost());
        assertThat(statisticEntityArgumentCaptor.getValue().getTotalOrderCount()).isEqualTo(1);

    }

    @Test
    void it_should_update_current_entry_of_month() {
        // given
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(7L);
        customerEntity.setEmail("test@test.com");
        customerEntity.setPassword("123456");
        customerEntity.setRoles(Set.of(new SimpleGrantedAuthority(DEFAULT_ROLE)));

        CustomerDetails customerDetails = CustomerDetails.getUserDetails(customerEntity);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setOrderPlacedDate(LocalDateTime.of(2021, Month.DECEMBER, 6, 22, 22));
        orderEntity.setPurchasedBookCount(3);
        orderEntity.setTotalCost(150.0);

        String EMAIL = "test@test.com";
        StatisticEntity statisticEntity = new StatisticEntity();
        statisticEntity.setId(1L);
        statisticEntity.setEmail(EMAIL);
        statisticEntity.setMonth("Dec");
        statisticEntity.setTotalPurchasedAmount(150.50);
        statisticEntity.setTotalBookCount(50);
        statisticEntity.setTotalOrderCount(7);

        List<StatisticEntity> usersAllStatistics = List.of(statisticEntity);
        given(statisticRepository.getStatisticEntityByEmail(customerDetails.getEmail())).willReturn(usersAllStatistics);

        // when
        statisticService.updateStatisticsForCustomer(customerDetails, orderEntity);

        // then
        ArgumentCaptor<StatisticEntity> statisticEntityArgumentCaptor = ArgumentCaptor.forClass(StatisticEntity.class);
        verify(statisticRepository, times(1)).save(statisticEntityArgumentCaptor.capture());
        assertThat(statisticEntityArgumentCaptor.getValue().getTotalBookCount()).isEqualTo(statisticEntity.getTotalBookCount());
        assertThat(statisticEntityArgumentCaptor.getValue().getTotalPurchasedAmount()).isEqualTo(statisticEntity.getTotalPurchasedAmount());
        assertThat(statisticEntityArgumentCaptor.getValue().getTotalOrderCount()).isEqualTo(8);
    }

}