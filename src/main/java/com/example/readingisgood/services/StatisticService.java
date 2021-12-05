package com.example.readingisgood.services;

import com.example.readingisgood.exceptions.StatisticsNotFoundException;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.persistence.entitites.OrderEntity;
import com.example.readingisgood.persistence.entitites.SequenceEntity;
import com.example.readingisgood.persistence.entitites.StatisticEntity;
import com.example.readingisgood.persistence.repositories.StatisticRepository;
import com.example.readingisgood.persistence.services.SequenceService;
import com.example.readingisgood.security.CustomerDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.example.readingisgood.persistence.entitites.StatisticEntity.STATISTIC_SEQUENCE;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;
    private final SequenceService sequenceService;

    public ReadingIsGoodResponse<List<StatisticEntity>> getStatistics(CustomerDetails customerDetails) {
        List<StatisticEntity> statisticEntityList = statisticRepository.getStatisticEntityByEmail(customerDetails.getEmail());

        if (!statisticEntityList.isEmpty()) {
            return new ReadingIsGoodResponse<>(statisticEntityList);
        } else {
            throw new StatisticsNotFoundException();
        }
    }

    public void updateStatisticsForCustomer(CustomerDetails customerDetails, OrderEntity orderEntity) {

        List<StatisticEntity> usersAllStatistics = statisticRepository.getStatisticEntityByEmail(customerDetails.getEmail());

        List<StatisticEntity> thisMonthsStatistics = usersAllStatistics
                .stream()
                .filter(p -> p.getMonth().equals(LocalDateTime.now().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)))
                .collect(Collectors.toList());

        StatisticEntity statisticEntity = null;

        if (thisMonthsStatistics.isEmpty()) {
             statisticEntity = setNewStatisticsEntity(orderEntity,customerDetails);
        } else if (!usersAllStatistics.isEmpty()) {
            statisticEntity = usersAllStatistics.get(0);
            statisticEntity.setTotalBookCount(statisticEntity.getTotalBookCount() + orderEntity.getPurchasedBookCount());
            statisticEntity.setTotalOrderCount(statisticEntity.getTotalOrderCount() + 1);
            statisticEntity.setTotalPurchasedAmount(statisticEntity.getTotalPurchasedAmount() + orderEntity.getTotalCost());
        }

        assert statisticEntity != null;
        statisticRepository.save(statisticEntity);
    }

    private StatisticEntity setNewStatisticsEntity(OrderEntity orderEntity, CustomerDetails customerDetails){
        StatisticEntity statisticEntity = new StatisticEntity();
        statisticEntity.setId(sequenceService.getNextSequence(STATISTIC_SEQUENCE));
        statisticEntity.setEmail(customerDetails.getEmail());
        statisticEntity.setTotalBookCount(statisticEntity.getTotalBookCount() + orderEntity.getPurchasedBookCount());
        statisticEntity.setTotalOrderCount(statisticEntity.getTotalOrderCount() + 1);
        statisticEntity.setTotalPurchasedAmount(statisticEntity.getTotalPurchasedAmount() + orderEntity.getTotalCost());
        statisticEntity.setMonth(LocalDateTime.now().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        return statisticEntity;
    }


}
