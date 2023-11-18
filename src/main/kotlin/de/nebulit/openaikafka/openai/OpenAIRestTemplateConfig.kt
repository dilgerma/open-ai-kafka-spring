package de.nebulit.openaikafka.openai

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate

@Configuration
class OpenAIRestTemplateConfig {

    @Value("\${open-ai.token}")
    private lateinit var token:String

    @Bean
    @Primary
    @Qualifier("open-ai-template")
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.interceptors.add(ClientHttpRequestInterceptor { request, body, execution ->
            request.headers.setBearerAuth(token)
            execution.execute(request, body)

        })
        return restTemplate
    }
}
