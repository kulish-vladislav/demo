package com.example.demo.filter;

import org.springframework.util.StreamUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author Vladislav Kulish on 22.12.2020
 */
@lombok.RequiredArgsConstructor
public class RequestResponseLoggingFilter implements Filter {

    private final LogWriter logWriter;

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpRequest);


        final HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        final ByteArrayResponseWrapper responseWrapper = new ByteArrayResponseWrapper(httpResponse);

        filterChain.doFilter(bufferedRequest, responseWrapper);

        byte[] responseBytes = responseWrapper.toByteArray();
        httpResponse.getOutputStream().write(responseBytes);

        logWriter.write(bufferedRequest.getBuffer(), httpRequest, responseBytes, httpResponse);
    }

    private static class ByteArrayResponseWrapper extends HttpServletResponseWrapper {

        private ByteArrayOutputStream baos = new ByteArrayOutputStream();
        private ServletOutputStream sos;
        private PrintWriter pw = new PrintWriter(baos);

        ByteArrayResponseWrapper(HttpServletResponse response) {
            super(response);
            sos = new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener listener) {
                }

                @Override
                public void write(int i) {
                    baos.write(i);
                }
            };
        }

        @Override
        public ServletOutputStream getOutputStream() {
            return sos;
        }

        public PrintWriter getWriter() {
            return pw;
        }

        byte[] toByteArray() {
            return baos.toByteArray();
        }
    }

    private static class BufferedRequestWrapper extends HttpServletRequestWrapper {

        private ByteArrayInputStream bais;
        private byte[] buffer;

        BufferedRequestWrapper(HttpServletRequest req) throws IOException {
            super(req);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                StreamUtils.copy(req.getInputStream(), baos);
                buffer = baos.toByteArray();
            }
            this.bais = new ByteArrayInputStream(this.buffer);
        }

        @Override
        public ServletInputStream getInputStream() {
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return bais.available() <= 0;
                }

                @Override
                public boolean isReady() {
                    return bais.available() > 0;
                }

                @Override
                public void setReadListener(ReadListener listener) {
                }

                @Override
                public int read() {
                    return bais.read();
                }
            };
        }

        byte[] getBuffer() {
            return this.buffer;
        }

    }

}