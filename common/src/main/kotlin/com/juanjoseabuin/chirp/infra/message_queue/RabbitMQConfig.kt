@file:Suppress("DEPRECATION", "REMOVAL")
package com.juanjoseabuin.chirp.infra.message_queue

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.juanjoseabuin.chirp.domain.event.ChirpEvent
import com.juanjoseabuin.chirp.domain.event.chat.ChatEventConstants
import com.juanjoseabuin.chirp.domain.event.user.UserEventsConstants
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class RabbitMQConfig {

    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter {
        val objectMapper = ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
            registerModule(JavaTimeModule())
            findAndRegisterModules()

            val polymorphicTypeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(ChirpEvent::class.java)
                .allowIfSubType("java.util") //Allow Java lists
                .allowIfSubType("kotlin.collections") //Kotlin Collections
                .build()

            activateDefaultTyping(
                polymorphicTypeValidator,
                ObjectMapper.DefaultTyping.NON_FINAL
            )
        }

        return Jackson2JsonMessageConverter(objectMapper).apply {
            typePrecedence = Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID
        }
    }

    @Bean
    fun rabbitListenerContainerFactory(
        connectionFactory: ConnectionFactory,
        transactionManager: PlatformTransactionManager,
        messageConverter: Jackson2JsonMessageConverter
    ): SimpleRabbitListenerContainerFactory {
        return SimpleRabbitListenerContainerFactory().apply {
            this.setConnectionFactory(connectionFactory)
            this.setTransactionManager(transactionManager)
            this.setChannelTransacted(true)
            this.setMessageConverter(messageConverter)
        }
    }

    @Bean
    fun rabbitTemplate(
        connectionFactory: ConnectionFactory,
        messageConverter: Jackson2JsonMessageConverter
    ): RabbitTemplate {
        return RabbitTemplate(connectionFactory).apply {
            this.messageConverter = messageConverter
        }
    }

    @Bean
    fun userExchange() = TopicExchange(
        UserEventsConstants.USER_EXCHANGE,
        true,
        false
    )

    @Bean
    fun chatExchange() = TopicExchange(
        ChatEventConstants.CHAT_EXCHANGE,
        true,
        false
    )

    @Bean
    fun notificationUserEventsQueue() = Queue(
        MessageQueues.NOTIFICATION_USER_EVENTS,
        true
    )

    @Bean
    fun notificationChatEventsQueue() = Queue(
        MessageQueues.NOTIFICATION_CHAT_EVENTS,
        true
    )

    @Bean
    fun chatUserEventsQueue() = Queue(
        MessageQueues.CHAT_USER_EVENTS,
        true
    )


    @Bean
    fun notificationUserEventsBinding(
        notificationUserEventsQueue: Queue,
        userExchange: TopicExchange
    ): Binding {
        return BindingBuilder
            .bind(notificationUserEventsQueue)
            .to(userExchange)
            .with("user.*")
    }

    @Bean
    fun notificationChatEventsBinding(
        notificationChatEventsQueue: Queue,
        chatExchange: TopicExchange
    ): Binding {
        return BindingBuilder
            .bind(notificationChatEventsQueue)
            .to(chatExchange)
            .with(ChatEventConstants.CHAT_NEW_MESSAGE)
    }

    @Bean
    fun chatUserEventsBinding(
        chatUserEventsQueue: Queue,
        userExchange: TopicExchange
    ): Binding {
        return BindingBuilder
            .bind(chatUserEventsQueue)
            .to(userExchange)
            .with("user.*")
    }
 }