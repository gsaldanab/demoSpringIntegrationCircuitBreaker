package com.geo.demospringintegrationcircuitbreaker.exception;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

public class OwnException extends MessagingException{

	public OwnException(String description) {
		super(description);
	}
	
	public OwnException(Message<?> message, String description) {
		super(message, description);
	}

}
