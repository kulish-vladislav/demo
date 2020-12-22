package com.example.demo.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vladislav Kulish on 22.12.2020
 */
public interface LogWriter {

    void write(byte[] requestBody, HttpServletRequest request,
               byte[] responseBody, HttpServletResponse response);

}
