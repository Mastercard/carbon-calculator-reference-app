package com.mastercard.developers.carboncalculator.exception;

import org.apache.commons.lang3.StringUtils;
import org.openapitools.client.model.ErrorWrapper;
import org.openapitools.client.model.Errors;
import org.openapitools.client.model.Error;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String SOURCE = "Carbon-Calculator";
    public static final String BAD_REQUEST_DESCRIPTION = "One of the request parameters is invalid, try again with correct request.";
    public static final String REASON_CODE_INVALID_REQUEST_PARAMETER = "INVALID_REQUEST_PARAMETER";

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.error("Exception", ex);

        String error = StringUtils.substringBefore(ex.getMessage(), ":");

        List<String> errors = new ArrayList<>();
        errors.add((error != null) ? (error) : (ex.getMessage()));
        return new ResponseEntity<>(getBadRequestError(errors), headers, HttpStatus.BAD_REQUEST);
    }

        private ErrorWrapper getBadRequestError(List<String> errors) {
           List<Error> errorList = new ArrayList<>();

        for (Iterator<String> iterator = errors.iterator(); iterator.hasNext(); ) {
            String errorMsg = iterator.next();


            errorList.add(getMcErrorResponse(SOURCE, BAD_REQUEST_DESCRIPTION, false,
                    REASON_CODE_INVALID_REQUEST_PARAMETER, errorMsg));
        }

        return getErrorResponse(errorList);
    }


    private Error getMcErrorResponse(String source, String description, boolean b,
                                     String reasonCode, String details) {
        Error error = new Error();
        error.setSource(source);
        error.setReasonCode(reasonCode);
        error.setDescription(description);
        error.setRecoverable(b);
        error.setDetails(details);

        return error;
    }

    public static ErrorWrapper getErrorResponse(List<Error> errorList) {
        Errors errors = new Errors();
        errors.setError(errorList);

        ErrorWrapper errorResponse = new ErrorWrapper();
        errorResponse.setErrors(errors);
        return errorResponse;
    }

}
