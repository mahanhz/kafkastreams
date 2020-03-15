package com.github.mahanhz.kafkastreams.stateful;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.github.mahanhz.kafkastreams.util.CarUtil.key;

@ConditionalOnProperty(value = "my-app.transformer", havingValue = "unpredictable")
@Component
public class TransformAndAggregateUnpredictable {

    public static final String CAR_SALE_STATS_STORE = "car-sale-stats-store";
    private final ObjectMapper objectMapper;
    private final long retentionMs;

    public TransformAndAggregateUnpredictable(final ObjectMapper objectMapper,
                                              @Value("${my-app.retention-ms}") final long retentionMs) {
        this.objectMapper = objectMapper;
        this.retentionMs = retentionMs;
    }

    @StreamListener(CarProcessor.CARS)
    public void process(final KStream<?, Car> input) {
        final Serde<Car> carJsonSerde = new JsonSerde<>(Car.class, objectMapper);

        final Duration retention = Duration.ofMillis(retentionMs);

        input.selectKey((s, car) -> key(car))
             .transform(() -> new MyTransformer<>("UNPREDICTABLE"))
             .groupByKey(Grouped.with(Serdes.String(), carJsonSerde))
             .windowedBy(TimeWindows.of(retention))
             .aggregate(CarSaleStatistic::init,
                        (key, car, aggregate) -> aggregateCars(aggregate, car),
                        Materialized.<String, CarSaleStatistic>as(Stores.persistentWindowStore(CAR_SALE_STATS_STORE, retention, retention, false))
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(CarSaleStatistic.class)));
    }

    private CarSaleStatistic aggregateCars(final CarSaleStatistic aggregate,
                                          final Car car) {
        System.out.println("Aggregating car: " + car);

        long quantity = 1L;
        if (aggregate != null) {
            quantity += aggregate.getQuantity();
        }

        return CarSaleStatistic.of(car.make, car.year, quantity);
    }
}
