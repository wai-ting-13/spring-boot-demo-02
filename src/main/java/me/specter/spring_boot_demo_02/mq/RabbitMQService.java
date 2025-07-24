package me.specter.spring_boot_demo_02.mq;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.specter.spring_boot_demo_02.entity.book.BookDto;
import me.specter.spring_boot_demo_02.entity.book.BookService;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQService {

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

    @Value("${application.rabbitmq.api-key}")
    private String API_KEY;
    
    private final RabbitTemplate rabbitTemplate;

    private final BookService bookService;


    @RabbitListener(queues = "${application.rabbitmq.request-queue-name}")
    public void receiveRequest(String key) {
        log.info("Request received -> %s".formatted(key));
        try {
            this.validateAPIKey(key);
            List<BookDto> books = this.bookService.findByCreatedAtToday();
            this.sendReply(books);
        } catch (Exception e) {
            log.info("Exception during consuming request:");
            log.info(e.getMessage());
        }
    }

    public void sendReply(List<BookDto> books){
        rabbitTemplate.convertAndSend(this.EXCHANGE_NAME, this.REP_R_KEY, books);
    }

    // Simulation of External Request
    public void sendRequest(String message) {
        log.info("Request send -> %s".formatted(message));
        rabbitTemplate.convertAndSend(this.EXCHANGE_NAME, this.REQ_R_KEY, message);
    }

    @RabbitListener(queues = "${application.rabbitmq.reply-queue-name}")
    public void receiveRely(List<BookDto> books) {
        log.info("Reply received -> %s");
        books.forEach( b-> {
            log.info("Data: %s".formatted(b.toString()));
        });
    }

    private void validateAPIKey(String key) throws IllegalArgumentException{
        if (!this.API_KEY.equals(key)){
           throw new IllegalArgumentException("API Key is not valid");
        }
    }

}
