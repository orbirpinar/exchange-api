package com.orbirpinar.exchange.exception;

import com.orbirpinar.exchange.util.ApiErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ApiError(BAD_REQUEST, BAD_REQUEST.value(), error, ex));
    }


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiError(UNSUPPORTED_MEDIA_TYPE,
                UNSUPPORTED_MEDIA_TYPE.value(), builder.substring(0, builder.length() - 2),
                ex,
                ApiErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setStatusCode(BAD_REQUEST.value());
        apiError.setMessage("Validation error");
        apiError.setErrorCode(ApiErrorCode.VALIDATION_ERROR.getCode());
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setStatusCode(BAD_REQUEST.value());
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String message = "Malformed JSON request";
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setStatusCode(BAD_REQUEST.value());
        apiError.setMessage(message);
        apiError.setDebugMessage(ex.getLocalizedMessage());
        apiError.setErrorCode(ApiErrorCode.MALFORMED_JSON.getCode());
        return buildResponseEntity(apiError);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.value(), error, ex));
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setStatusCode(BAD_REQUEST.value());
        apiError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setStatusCode(BAD_REQUEST.value());
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        apiError.setErrorCode(ApiErrorCode.TYPE_MISMATCH.getCode());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ExchangeClientException.class)
    public ResponseEntity<Object> handleExchangeClientException(ExchangeClientException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setStatusCode(BAD_REQUEST.value());
        apiError.setMessage("Exchange Client Error");
        ExternalApiError externalApiError = new ExternalApiError(ex.getMessage(), ex.getErrorCode());
        apiError.addSubError(externalApiError);
        apiError.setErrorCode(ApiErrorCode.EXCHANGE_RATE_CLIENT_ERROR.getCode());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setStatusCode(BAD_REQUEST.value());
        apiError.setMessage(ex.getMessage());
        apiError.setErrorCode(ApiErrorCode.RESOURCE_NOT_FOUND.getCode());
        return buildResponseEntity(apiError);
    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
