package com.example.demo.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author Vladislav Kulish on 22.12.2020
 */
@Slf4j
public class SimpleLogWriter implements LogWriter {

    public void write(byte[] requestBody, HttpServletRequest request,
                      byte[] responseBody, HttpServletResponse response) {
        log.info("request: {uri:{}, method:{}, body:{}}; response: {status:{}, body:{}}",
                request.getRequestURL(),
                request.getMethod(),
                toString(requestBody),
                response.getStatus(),
                toString(responseBody));
    }

    private String toString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8).replaceAll("[\\r\\n\\t]", "");
    }

}
