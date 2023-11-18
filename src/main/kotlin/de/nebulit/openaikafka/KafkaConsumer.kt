package de.nebulit.openaikafka

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.math.BigDecimal


@Component
class KafkaConsumer {

    val logger = KotlinLogging.logger {}

    @KafkaListener(containerFactory = "openAiContainerFactory", groupId = "test-group", topics = ["test-topic"])
    fun consume(payment: Payment) {
        logger.info { "Processing Record :  Age: ${payment.age}, Amount: ${payment.amount}" }
    }
}
