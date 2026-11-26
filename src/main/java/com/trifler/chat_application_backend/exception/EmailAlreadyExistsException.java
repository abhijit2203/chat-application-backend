package com.trifler.chat_application_backend.exception;

public class EmailAlreadyExistsException extends RuntimeException{
	
	 public EmailAlreadyExistsException(String message) {
	        super(message);
	    }

}
