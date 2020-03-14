package com.github.mahanhz.kafkastreams.stateful;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.Stores;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AggregateStream {

    public static final String CAR_SALE_STATS_STORE = "car-sale-stats-store";
    private ObjectMapper objectMapper;

    public AggregateStream(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @StreamListener(CarProcessor.CARS)
    public void process(final KStream<?, Car> input) {
        // The car sale statistic are in json format
        final Serde<CarSaleStatistic> carSalesJsonSerde = new JsonSerde<>(CarSaleStatistic.class, objectMapper);

        final Duration retention = Duration.ofSeconds(60);

        input.groupBy((s, car) -> car.getYear() + "_" + car.getMake())
             .windowedBy(TimeWindows.of(retention))
             .aggregate(CarSaleStatistic::init,
                        (key, car, aggregate) -> aggregateCars(aggregate, car),
                        Materialized.<String, CarSaleStatistic>as(Stores.persistentWindowStore(CAR_SALE_STATS_STORE, retention, retention, false))
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(CarSaleStatistic.class)));
    }

    public CarSaleStatistic aggregateCars(final CarSaleStatistic aggregate,
                                          final Car car) {
        long quantity = 1L;
        if (aggregate != null) {
            quantity += aggregate.getQuantity();
        }

        return CarSaleStatistic.of(car.make, car.year, quantity);
    }
}
