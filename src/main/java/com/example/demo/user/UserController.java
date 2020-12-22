package com.example.demo.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Vladislav Kulish on 22.12.2020
 */
@RestController
@RequestMapping("/api/user")
@lombok.RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserInfo getUserInfo(@Valid @RequestBody UserRequestData userRequestData) {

        return new UserInfo(userService.getUserFio(userRequestData.getId()));
    }

    @lombok.Value
    @lombok.Builder(builderClassName = "Builder")
    @JsonDeserialize(builder = UserRequestData.Builder.class)
    private static class UserRequestData {

        @NotNull
        Integer id;

        @JsonPOJOBuilder(withPrefix = "")
        static class Builder {
        }
    }

    @lombok.Value
    private static class UserInfo {

        String fio;

    }
}
