package me.specter.spring_boot_demo_02.rateLimiting;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;

@Configuration
public class RateLimitConfig {

    @Value("${application.redis.host}")
    private String REDIS_HOST;

    @Value("${application.redis.port}")
    private Integer REDIS_PORT;

    @Bean ProxyManager<String> lettuceBasedProxyManager(RedisClient redisClient) {
        StatefulRedisConnection<String, byte[]> redisConnection = redisClient
            .connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return LettuceBasedProxyManager.builderFor(redisConnection)
            .withExpirationStrategy(
                ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(1L)))
            .build();
    }

    @Bean RedisClient redisClient(){
        return RedisClient.create(
            RedisURI.builder()
                .withHost(this.REDIS_HOST)
                .withPort(this.REDIS_PORT)
                .withSsl(false)
                .build()
        );
    }

    @Bean Supplier<BucketConfiguration> bucketConfiguration() {
        return () -> BucketConfiguration.builder()
            .addLimit(
                Bandwidth
                    .builder()
                    .capacity(2L)
                    .refillIntervally(2L, Duration.ofMinutes(1L))
                    .build()
            ).build();
    }
}
