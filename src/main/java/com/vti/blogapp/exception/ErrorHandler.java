package com.vti.blogapp.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.HashMap;

@ControllerAdvice
public class ErrorHandler
        extends ResponseEntityExceptionHandler
        implements MessageSourceAware, AuthenticationEntryPoint, AccessDeniedHandler {
    private MessageSource messageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        var message = "Sorry! Invalid form";
        var errors = new HashMap<String, String>();
        for (var error : exception.getFieldErrors()) {
            var key = error.getField();
            var value = error.getDefaultMessage();
            errors.put(key, value);
        }
        var response = new ErrorResponse(message, errors);
        return new ResponseEntity<>(response, headers, status);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception) {
        var message = "Sorry! Invalid form";
        var errors = new HashMap<String, String>();
        for (var constrain : exception.getConstraintViolations()) {
            var key  = constrain.getPropertyPath().toString();
            var value = constrain.getMessage();
            errors.put(key, value);
        }
        var response = new ErrorResponse(message, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        var message = getMessage("AuthenticationException.message");
        var error = new ErrorResponse(message);
        var out = response.getOutputStream();
        new ObjectMapper().writeValue(out, error);
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response, AccessDeniedException exception
    ) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        var message = getMessage("AccessDeniedException.message");
        var error = new ErrorResponse(message);
        var out = response.getOutputStream();
        new ObjectMapper().writeValue(out, error);
    }
}
