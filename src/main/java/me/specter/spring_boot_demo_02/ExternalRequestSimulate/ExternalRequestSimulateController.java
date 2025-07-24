package me.specter.spring_boot_demo_02.ExternalRequestSimulate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExternalRequestSimulateController {
    
    private final ExternalRequestSimulateService externalRequestSimulateService;

    @GetMapping("/send")
    public void send(@RequestParam String key) {
        externalRequestSimulateService.send(key);
    }
    

}
