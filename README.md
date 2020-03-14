# Kafka streams topics

<application-name>-<store-name>-changelog

<application-name>-<store-name>-repartition

 
# Kafkacat commands

kafkacat -b localhost:9092 -t kafka-streams-gotchas-car-sale-stats-store-changelog

kafkacat -b localhost:9092 -t kafka-streams-gotchas-car-sale-stats-store-repartition

**Consume from topic**: kafkacat -C -b localhost:9092 -t cars-topic

**Write to topic**: kafkacat -b localhost:9092 -t car-topic -T -P -l test-cars.txt