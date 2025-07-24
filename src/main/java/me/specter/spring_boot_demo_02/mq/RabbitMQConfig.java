package me.specter.spring_boot_demo_02.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${application.rabbitmq.exchange-name}")
    private String EXCHANGE_NAME;

    @Value("${application.rabbitmq.request-queue-name}")
    private String REQ_QUEUE_NAME;

    @Value("${application.rabbitmq.reply-queue-name}")
    private String REP_QUEUE_NAME;

    @Value("${application.rabbitmq.reply-routing-key}")
    private String REQ_R_KEY;

    @Value("${application.rabbitmq.request-routing-key}")
    private String REP_R_KEY;

    // Set JSON Converter to rabbitTemplate
    @Bean RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean DirectExchange latestBookExchange() {
        return new DirectExchange(this.EXCHANGE_NAME);
    }

    @Bean Queue requestQueue() {
        return new Queue(this.REQ_QUEUE_NAME, true);
    }

    @Bean Queue replyQueue() {
        return new Queue(this.REP_QUEUE_NAME, true);
    }

    @Bean Binding requestBinding(Queue requestQueue, DirectExchange latestBookExchange) {
        return BindingBuilder.bind(requestQueue)
                            .to(latestBookExchange)
                            .with(this.REQ_R_KEY);
    }

    @Bean Binding replyBinding(Queue replyQueue, DirectExchange latestBookExchange) {
        return BindingBuilder.bind(replyQueue)
                            .to(latestBookExchange)
                            .with(this.REP_R_KEY);
    }

    // JSON Converter
    @Bean MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
