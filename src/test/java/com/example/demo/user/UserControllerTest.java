package com.example.demo.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Vladislav Kulish on 22.12.2020
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    private static final String TEST_FIO = "Testov Test";
    private static final String API_USER_URL = "/api/user";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userServiceMock;

    @Test
    void getUserInfo() throws Exception {
        int testId = 1;

        when(userServiceMock.getUserFio(anyInt())).thenReturn(TEST_FIO);

        mvc.perform(post(API_USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"id\": " + testId + "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"fio\":\"" + TEST_FIO + "\"}", true));

        verify(userServiceMock).getUserFio(testId);
    }

    @Test
    void getUserInfoWhenNull() throws Exception {
        int testId = 2;

        when(userServiceMock.getUserFio(anyInt())).thenReturn(null);

        mvc.perform(post(API_USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"id\": " + testId + "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"fio\": null}", true));

        verify(userServiceMock).getUserFio(testId);
    }

    @Test
    void getUserInfoWhenBadRequest() throws Exception {
        int testId = 2;

        when(userServiceMock.getUserFio(anyInt())).thenReturn(null);

        mvc.perform(post(API_USER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"idd\": " + testId + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\": \"must not be null\"}", true));

        verifyNoInteractions(userServiceMock);
    }
}