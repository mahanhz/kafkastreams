package com.github.mahanhz.kafkastreams.adapter.inbound.rest;

import com.github.mahanhz.kafkastreams.stateful.CarSaleStatistic;
import com.github.mahanhz.kafkastreams.stateful.StoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final StoreService storeService;

    public StatisticsController(final StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{year}/{carMake}")
    public CarSaleStatistic aggregation(@PathVariable final String year,
                                        @PathVariable final String carMake) {
        return storeService.carSaleStatistic(year, carMake);
    }
}
