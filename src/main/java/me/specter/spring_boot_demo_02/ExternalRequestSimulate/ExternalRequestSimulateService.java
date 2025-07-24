package me.specter.spring_boot_demo_02.ExternalRequestSimulate;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.specter.spring_boot_demo_02.mq.RabbitMQService;

@Service
@RequiredArgsConstructor
public class ExternalRequestSimulateService {
    
    private final RabbitMQService rabbitMQService;

    public void send(String key) {
        rabbitMQService.sendRequest(key);
    }
    
}
