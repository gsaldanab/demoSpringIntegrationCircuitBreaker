package com.geo.demospringintegrationcircuitbreaker.config;

import org.springframework.integration.handler.advice.RequestHandlerCircuitBreakerAdvice;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import com.geo.demospringintegrationcircuitbreaker.exception.OwnException;

public class DemoCircuitBreakerAdvice extends RequestHandlerCircuitBreakerAdvice{

	@Override
	protected Object doInvoke(ExecutionCallback callback, Object target, Message<?> message) {
		try {
			return super.doInvoke(callback, target, message);
		} catch(Exception ex) {
			if(ex instanceof CircuitBreakerOpenException) {
				throw handleCircuitBreakerOpenException(message, "error controlado");
			}
			throw ex;
		}
	}

	private OwnException handleCircuitBreakerOpenException(Message<?> message, String mensaje) {
		
		return new OwnException(message, mensaje);
	}
}
