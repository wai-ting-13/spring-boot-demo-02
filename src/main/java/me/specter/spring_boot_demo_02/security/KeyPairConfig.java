package me.specter.spring_boot_demo_02.security;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyPairConfig {
    @Bean PrivateKey privateKey() throws Exception{
        return KeyUtils.loadPrivateKey("keys/private_key.pem");
    }

    @Bean PublicKey publicKey() throws Exception{
        return KeyUtils.loadPublicKey("keys/public_key.pem");
    }
}
