package me.specter.spring_boot_demo_02.aspect;

import java.util.function.Supplier;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.specter.spring_boot_demo_02.exception.TooManyRequestException;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimitAspect {
    
    private final ProxyManager<String> proxyManager;
    private final Supplier<BucketConfiguration> bucketConfiguration;

    @Before("@annotation(RateLimited)")
    public void enforceRateLimit(JoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ) {
            throw new RuntimeException("Authentication Object is null");
        }

        String username = authentication.getName();
        Bucket bucket = this.proxyManager.builder().build(username, this.bucketConfiguration);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (!probe.isConsumed()) {
            log.info("user(%s) exceeded bucket limit".formatted(username));
            throw new TooManyRequestException("Too many request. Try again after 1 minute");
        }
    }
}
