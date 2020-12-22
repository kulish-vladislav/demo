package com.example.demo.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vladislav Kulish on 22.12.2020
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String TEST_FIO = "Test Testov";
    private static final String TEST_ENCRYPT_VALUE = "test-encrypt-value";

    @Mock
    private UserCryptoService userCryptoServiceMock;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        when(userCryptoServiceMock.encrypt(anyString())).thenReturn(TEST_ENCRYPT_VALUE);
        userService.init();
    }

    @Test
    void getUserFioWhenIdOneThenResult() {
        when(userCryptoServiceMock.decrypt(anyString())).thenReturn(TEST_FIO);

        String actual = userService.getUserFio(1);

        verify(userCryptoServiceMock).decrypt(TEST_ENCRYPT_VALUE);

        assertThat(actual).isEqualTo(TEST_FIO);
    }

    @Test
    void getUserFioWhenIdNotOneThenNull() {
        String actual = userService.getUserFio(2);

        verify(userCryptoServiceMock, Mockito.times(0)).decrypt(Mockito.anyString());

        assertThat(actual).isNull();
    }
}