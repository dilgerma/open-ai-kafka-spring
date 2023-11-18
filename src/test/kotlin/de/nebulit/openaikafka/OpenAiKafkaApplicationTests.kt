package de.nebulit.openaikafka

import org.awaitility.Awaitility
import org.junit.Assert
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.time.Duration
import kotlin.random.Random

@SpringBootTest(classes = [TestOpenAiKafkaApplication::class])
@Testcontainers
class OpenAiKafkaApplicationTests {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, Payment>

    @Test
    fun contextLoads() {
        (1..10).forEach {
            kafkaTemplate.send("test-topic", "test", Payment().apply {
                this.age = Random.nextInt(from = 5, until = 40)
                this.amount = BigDecimal(Random.nextDouble(from = 5.toDouble(), until = 999.toDouble()))
            })
        }
        Awaitility.await().pollDelay(Duration.ofMinutes(2))
            .timeout(Duration.ofMinutes(3))
            .untilAsserted { assertTrue(true) };
    }

}
