package com.example.readingisgood.controllers;

import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.persistence.entitites.StatisticEntity;
import com.example.readingisgood.security.CustomerDetails;
import com.example.readingisgood.services.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticService statisticService;

    @GetMapping
    public ReadingIsGoodResponse<List<StatisticEntity>> getStatistics(@AuthenticationPrincipal CustomerDetails customerDetails){
        return statisticService.getStatistics(customerDetails);
    }
}
