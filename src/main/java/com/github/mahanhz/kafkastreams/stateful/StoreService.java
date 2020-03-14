package com.github.mahanhz.kafkastreams.stateful;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

import static com.github.mahanhz.kafkastreams.stateful.AggregateStream.CAR_SALE_STATS_STORE;
import static org.apache.kafka.streams.state.StreamsMetadata.NOT_AVAILABLE;

@Service
public class StoreService {

    private final InteractiveQueryService interactiveQueryService;
    private final RestTemplate restTemplate;

    public StoreService(final InteractiveQueryService interactiveQueryService, final RestTemplateBuilder restTemplateBuilder) {
        this.interactiveQueryService = interactiveQueryService;
        this.restTemplate = restTemplateBuilder.build();
    }

    public CarSaleStatistic carSaleStatistic(final String year, final String carMake) {
        final ReadOnlyWindowStore<String, CarSaleStatistic> queryableStore = queryableStore();

        final String key = key(year, carMake);
        final HostInfo hostInfo = hostInfo(key);

        if (hostInfo != null) {
            if (queryableStore != null && interactiveQueryService.getCurrentHostInfo().equals(hostInfo)) {
                return carSaleStatisticFromStore(queryableStore, key);
            }

            return restTemplate.getForObject(String.format("http://%s:%d/statistics/stores/local/%s/%s",
                                                           hostInfo.host(),
                                                           hostInfo.port(),
                                                           year,
                                                           carMake),
                                             CarSaleStatistic.class);
        }

        return null;
    }

    public CarSaleStatistic carSaleStatisticInLocalStore(final String year, final String carMake) {
        final ReadOnlyWindowStore<String, CarSaleStatistic> queryableStore = queryableStore();

        if (queryableStore != null) {
            return carSaleStatisticFromStore(queryableStore, key(year, carMake));
        }

        return null;
    }

    private String key(final String year, final String carMake) {
        return year + "_" + carMake;
    }

    private CarSaleStatistic carSaleStatisticFromStore(final ReadOnlyWindowStore<String, CarSaleStatistic> queryableStore, final String key) {
        try (final WindowStoreIterator<CarSaleStatistic> iterator = queryableStore.fetch(key, Instant.EPOCH, Instant.now())) {
            KeyValue<Long, CarSaleStatistic> carSaleStatistic = null;
            while (iterator.hasNext()) {
                carSaleStatistic = iterator.next();
            }

            return carSaleStatistic.value;
        }
    }

    private HostInfo hostInfo(final String key) {
        final HostInfo hostInfo = interactiveQueryService.getHostInfo(CAR_SALE_STATS_STORE, key, new StringSerializer());

        System.out.println("Host info: " + hostInfo);

        if (hostInfo == null || hostInfo.equals(NOT_AVAILABLE.hostInfo())) {
            return null;
        }

        return hostInfo;
    }

    private ReadOnlyWindowStore<String, CarSaleStatistic> queryableStore() {
        try {
            return interactiveQueryService.getQueryableStore(CAR_SALE_STATS_STORE, QueryableStoreTypes.windowStore());
        } catch (Exception ex) {
            System.out.println("Unable to get queryable store " + CAR_SALE_STATS_STORE + "." + ex);
        }

        return null;
    }
}
