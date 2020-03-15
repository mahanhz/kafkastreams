# Get the application running

#### Prerequisites
* Docker - https://docs.docker.com/docker-for-mac/
* Kafkacat - https://github.com/edenhill/kafkacat
* Kafka client - https://kafka.apache.org/downloads#2.3.1

#### Running the application
* **Start Kafka:** docker-compose up -d
* **Add messages to the cars-topic:** kafkacat -b localhost:9092 -t cars-topic -T -P -l test-cars.txt
* **Start the application:** ./gradlew bootRun
* **Check if the the data was aggregated:** http://localhost:12001/statistics/2020/Volvo

# Kafka commands

**Describe the kafka topics**  
./kafka-topics.sh --zookeeper localhost:2181 --describe

**Update partitions**  
./kafka-topics.sh --topic cars-topic --alter --partitions 50 --zookeeper localhost:2181

**Update retention**  
./kafka-configs.sh --entity-type topics --entity-name kafka-streams-gotchas-car-sale-stats-store-changelog --alter --add-config retention.ms=300000 --zookeeper localhost:2181

**Reset streams application**  
./kafka-streams-application-reset.sh --application-id kafka-streams-gotchas

# Kafkacat commands

**Consume from changelog topic**
kafkacat -C -b localhost:9092 -t kafka-streams-gotchas-car-sale-stats-store-changelog

**Consume from repartition topic**
kafkacat -C -b localhost:9092 -t kafka-streams-gotchas-car-sale-stats-store-repartition

**Consume from topic**  
kafkacat -C -b localhost:9092 -t cars-topic

**Write to topic**  
kafkacat -b localhost:9092 -t cars-topic -T -P -l test-cars.txt