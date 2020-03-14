package com.github.mahanhz.kafkastreams;

import com.github.mahanhz.kafkastreams.stateful.CarProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(CarProcessor.class)
@SpringBootApplication
public class KafkastreamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkastreamsApplication.class, args);
	}

}
