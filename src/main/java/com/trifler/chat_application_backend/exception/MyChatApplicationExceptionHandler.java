package com.trifler.chat_application_backend.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class MyChatApplicationExceptionHandler {
	
	    @ExceptionHandler(ResponseStatusException.class)
	    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
	            ResponseStatusException ex) {
	    	
	    	//System.out.println("GLOBAL HANDLER CALLED: " + ex.getReason());

	        Map<String, Object> response = new HashMap<>();

	        response.put("status", ex.getStatusCode().value());
	        response.put("message", ex.getReason());

	        return ResponseEntity
	                .status(ex.getStatusCode())
	                .body(response);
	    }

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Map<String, Object>> handleValidationException(
	            MethodArgumentNotValidException ex) {

	        Map<String, Object> errors = new HashMap<>();
	        
			List<FieldError> oe=ex.getBindingResult().getFieldErrors();
			for(FieldError error:oe) {
				String fieldName=error.getField();
				String errorMessage=error.getDefaultMessage();
				errors.put(fieldName, errorMessage);
			}
	        
//	        List<ObjectError> oe= ex.getAllErrors();
//			for(ObjectError error:oe) {
//				FieldError fieldError=(FieldError) error;
//				String fieldName=fieldError.getField();
//				String errorMessage=fieldError.getDefaultMessage();
//				errors.put(fieldName, errorMessage);
//			}

	        

	        Map<String, Object> response = new HashMap<>();

	        response.put("status", HttpStatus.BAD_REQUEST.value());
	        response.put("message", "Validation failed");
	        response.put("errors", errors);

	        return ResponseEntity
	                .badRequest()
	                .body(response);
	    }
	    
	    @ExceptionHandler(EmailAlreadyExistsException.class)
	    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExists(
	            EmailAlreadyExistsException ex) {

	        Map<String, Object> response = new HashMap<>();

	        response.put("status", HttpStatus.CONFLICT.value());
	        response.put("message", ex.getMessage());

	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(response);
	    }
	    
	    @ExceptionHandler(UsernameAlreadyExistsException.class)
	    public ResponseEntity<Map<String, Object>> handleUsernameAlreadyExists(
	            UsernameAlreadyExistsException ex) {

	        Map<String, Object> response = new HashMap<>();

	        response.put("status", HttpStatus.CONFLICT.value());
	        response.put("message", ex.getMessage());

	        return ResponseEntity
	                .status(HttpStatus.CONFLICT)
	                .body(response);
	    }
	    
	    @ExceptionHandler(UserNotFoundException.class)
	    public ResponseEntity<Map<String, Object>> handleUserNotFound(
	            UserNotFoundException ex) {

	        Map<String, Object> response = new HashMap<>();

	        response.put("status", HttpStatus.NOT_FOUND.value());
	        response.put("message", ex.getMessage());

	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(response);
	    }
	    
	    @ExceptionHandler(BadCredentialsException.class)
	    public ResponseEntity<Map<String, Object>> handleBadCredentials(
	            BadCredentialsException ex) {

	        Map<String, Object> response = new HashMap<>();

	        response.put("status", HttpStatus.UNAUTHORIZED.value());
	        response.put("message", "Invalid email or password");

	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body(response);
	    }

}
