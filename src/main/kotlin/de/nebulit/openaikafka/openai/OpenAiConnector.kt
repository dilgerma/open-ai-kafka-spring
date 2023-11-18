package de.nebulit.openaikafka.openai

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import kotlin.system.measureTimeMillis


class Message {
    @JsonProperty("role")
    var role:String? = null
    @JsonProperty("content")
    var content: String? = null
}


class ChatResponse {
    @JsonProperty("choices")
    val choices: List<Choice>? = null

    // constructors, getters and setters
    class Choice {
        @JsonProperty("index")
        val index = 0
        @JsonProperty("message")
        val message: Message? = null // constructors, getters and setters
    }
}


class ChatRequest(messageList: List<Message>) {
    @JsonProperty("model")
    private val model: String="gpt-3.5-turbo"
    @JsonProperty("messages")
    private val messages: MutableList<Message> = mutableListOf()

    init {
        messages.addAll(messageList)
    } // getters and setters
}


@Component
class OpenAiConnector(@Value("\${open-ai.url}") var openAI:String,
                      @Qualifier("open-ai-template") var restTemplate: RestTemplate) {

    fun requestResponse(request: ChatRequest): String? {
        var result:ChatResponse?
        val duration = measureTimeMillis {
            result = restTemplate.postForObject(openAI, request, ChatResponse::class.java)
        }
        println("openAI call took $duration")
        return result?.choices?.firstOrNull()?.message?.content
    }
}
