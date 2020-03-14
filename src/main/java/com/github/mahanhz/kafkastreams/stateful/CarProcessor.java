package com.github.mahanhz.kafkastreams.stateful;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface CarProcessor {

    String CARS = "cars";

    @Input(CARS)
    KStream<?, Car> input();
}
