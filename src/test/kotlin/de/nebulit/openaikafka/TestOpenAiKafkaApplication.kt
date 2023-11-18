package de.nebulit.openaikafka

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName


@TestConfiguration(proxyBeanMethods = false)
class TestOpenAiKafkaApplication {
    @Bean
    @ServiceConnection
    fun kafkaContainer(): KafkaContainer {
        return KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
    }
}
