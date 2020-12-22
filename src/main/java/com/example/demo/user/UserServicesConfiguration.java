package com.example.demo.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

/**
 * @author Vladislav Kulish on 22.12.2020
 */
@Configuration
public class UserServicesConfiguration {

    @Bean
    public UserCryptoService userCryptoService(
            @Value("${user.secret.key.base64}") String keyAsBase64,
            @Value("${user.secret.iv.base64}") String ivAsBase64) {
        byte[] key = Base64.getDecoder().decode(keyAsBase64);
        byte[] iv = Base64.getDecoder().decode(ivAsBase64);

        return new UserCryptoService(key, iv);
    }


}
