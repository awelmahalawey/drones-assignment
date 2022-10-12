package com.musala.soft.drones.controller;

import com.musala.soft.drones.constant.WebConstants;
import com.musala.soft.drones.exception.ApiResponseErrorException;
import com.musala.soft.drones.exception.BaseRunTimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Service
public class BaseController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HttpServletRequest context;

    public ApiResponseErrorException apiResponseErrorException(
            BaseRunTimeException ex
    ) {
        ex.printStackTrace();
        String acceptLanguage = context.getHeader(WebConstants.HEADER_KEY_ACCEPT_LANGUAGE);
        if(!StringUtils.hasLength(acceptLanguage)) {
            acceptLanguage = "en";
        }
        String message = messageSource.getMessage(ex.getErrorName(), null, new Locale(acceptLanguage));
        return new ApiResponseErrorException(ex.getErrorName(), message, HttpStatus.BAD_REQUEST);
    }
}
