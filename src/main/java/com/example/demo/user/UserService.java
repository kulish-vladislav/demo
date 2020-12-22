package com.example.demo.user;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @author Vladislav Kulish on 22.12.2020
 */
@Service
@lombok.RequiredArgsConstructor
public class UserService {

    private Map<Integer, String> USER_MAP;
    private final UserCryptoService userCryptoService;

    @PostConstruct
    void init() {
        String fio = "Test Testov";
        USER_MAP = Collections.singletonMap(1, userCryptoService.encrypt(fio));
    }

    String getUserFio(Integer id) {
        return Optional.ofNullable(USER_MAP.get(id))
                .map(userCryptoService::decrypt)
                .orElse(null);
    }
}
