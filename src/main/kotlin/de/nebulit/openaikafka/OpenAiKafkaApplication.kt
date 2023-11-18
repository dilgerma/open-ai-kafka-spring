package de.nebulit.openaikafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.nebulit.openaikafka.openai.ChatRequest
import de.nebulit.openaikafka.openai.Message
import de.nebulit.openaikafka.openai.OpenAiConnector
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.listener.adapter.RecordFilterStrategy


class Response {
    var suspicious: Boolean = false
    var reasoning: String? = null
}

class OpenAiRecordFilterStrategy(var advice: String, var openAiConnector: OpenAiConnector) :
    RecordFilterStrategy<String, Payment> {
    override fun filter(record: ConsumerRecord<String, Payment>): Boolean {
        val result = jacksonObjectMapper().readValue(
            //call openAPI
            openAiConnector.requestResponse(
                //build prompt from record
                buildPromptFromRecord(record)
            ), Response::class.java
        )

        // use open ai provided information
        if (result.suspicious) {
            println("Suspicious record filtered out. Reason: ${result.reasoning}")
            // true means, record is filtered an not processed any further.
            return true
        }
        // process record regularly
        return false

    }

    private fun buildPromptFromRecord(record: ConsumerRecord<String, Payment>) =
        ChatRequest(
            listOf(
                Message().apply {
                    this.role = "system"
                    this.content = advice
                },
                Message().apply {
                    this.role = "user"
                    this.content =
                        jacksonObjectMapper().writeValueAsString(record.value())
                })
        )
}

@SpringBootApplication
class OpenAiKafkaApplication {

    @Autowired
    private lateinit var openAiConnector: OpenAiConnector

    @Value("\${openai.advice}")
    private lateinit var advice: String

    @Bean
    fun openAiRecordFilterStrategy(openAiConnector: OpenAiConnector): RecordFilterStrategy<String, Payment> {
        return OpenAiRecordFilterStrategy(advice, openAiConnector)
    }

    @Bean
    fun openAiContainerFactory(
        kafkaProperties: KafkaProperties,
        consumerFactory: ConsumerFactory<String, Payment>
    ): ConcurrentKafkaListenerContainerFactory<String, Payment> {
        var concurrentKafkaListenerContainerFactory = ConcurrentKafkaListenerContainerFactory<String, Payment>()
        concurrentKafkaListenerContainerFactory.setRecordFilterStrategy(openAiRecordFilterStrategy(openAiConnector))
        concurrentKafkaListenerContainerFactory.consumerFactory = consumerFactory
        return concurrentKafkaListenerContainerFactory
    }
}

fun main(args: Array<String>) {
    runApplication<OpenAiKafkaApplication>(*args)
}
