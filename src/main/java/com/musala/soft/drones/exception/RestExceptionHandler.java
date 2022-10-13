package com.musala.soft.drones.exception;

import com.musala.soft.drones.constant.MessageConstants;
import com.musala.soft.drones.model.ApiErrorResource;
import org.hibernate.AnnotationException;
import org.hibernate.MappingException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.hibernate.tool.schema.spi.SchemaManagementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.OptimisticLockException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;

@ControllerAdvice
public class RestExceptionHandler {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(ApiResponseErrorException.class)
	public ResponseEntity<ApiErrorResource> handleEmberException(ApiResponseErrorException ex) {
		LOGGER.error("ERROR: " + ex.getErrorName() + " - " + ex.getMessage());
		ApiErrorResource responseBody = new ApiErrorResource();
		responseBody.setErrorName(ex.getErrorName());
		responseBody.setErrorMessage(ex.getMessage());
		responseBody.setTimestamp(new Date().getTime());
		return ResponseEntity.status(ex.getStatusCode()).body(responseBody);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<ApiErrorResource> handleSQLException(Exception ex) {
		ex.printStackTrace();
		ApiErrorResource responseBody = getGeneralInternalServerErrorResponse();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}

	@ExceptionHandler(MappingException.class)
	public ResponseEntity<ApiErrorResource> handleMappingException(Exception ex) {
		ex.printStackTrace();
		ApiErrorResource responseBody = getGeneralInternalServerErrorResponse();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResource> handleConstraintViolationException(Exception ex) {
		ex.printStackTrace();
		ApiErrorResource responseBody = getGeneralInternalServerErrorResponse();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}

	private ApiErrorResource getGeneralInternalServerErrorResponse() {
		ApiErrorResource responseBody = new ApiErrorResource();
		responseBody.setErrorName(MessageConstants.INTERNAL_SERVER_ERROR);
		String message = messageSource.getMessage(MessageConstants.INTERNAL_SERVER_ERROR, null, new Locale("en"));
		responseBody.setErrorMessage(message);
		responseBody.setTimestamp(new Date().getTime());
		return responseBody;
	}
}
