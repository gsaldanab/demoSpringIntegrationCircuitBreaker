package com.geo.demospringintegrationcircuitbreaker.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.handler.advice.AbstractRequestHandlerAdvice;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import com.geo.demospringintegrationcircuitbreaker.model.ws.ResponseMsg;

public class CustomCircuitBreakerAdvice extends AbstractRequestHandlerAdvice  {


	public static final int DEFAULT_THRESHOLD = 5;
	public static final int DEFAULT_HALF_OPEN_AFTER = 1000;
	private final ConcurrentMap<Object, AdvisedMetadata> metadataMap = new ConcurrentHashMap<>();
	private int threshold = DEFAULT_THRESHOLD;
	private long halfOpenAfter = DEFAULT_HALF_OPEN_AFTER;

	
	private CustomCircuitBreakerByOption optionHandler;
	
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public void setHalfOpenAfter(long halfOpenAfter) {
		this.halfOpenAfter = halfOpenAfter;
	}
	

	@Override
	protected Object doInvoke(ExecutionCallback callback, Object target, Message<?> message) {
		AdvisedMetadata metadata = this.metadataMap.get(target);
		if (metadata == null) {
			this.metadataMap.putIfAbsent(target, new AdvisedMetadata());
			metadata = this.metadataMap.get(target);
		}
		if (metadata.getFailures().get() >= this.threshold &&
				System.currentTimeMillis() - metadata.getLastFailure() < this.halfOpenAfter) {
			throw new CircuitBreakerOpenException(message, "Circuit Breaker is Open for " + target);
		}
		Object result = null;
		try {
			result = callback.execute();
			if (logger.isDebugEnabled() && metadata.getFailures().get() > 0) {
				logger.debug("Closing Circuit Breaker for " + target);
			}
			metadata.getFailures().set(0);

		}
		catch (Exception e) {
			metadata.getFailures().incrementAndGet();
			metadata.setLastFailure(System.currentTimeMillis());
			if (e instanceof ThrowableHolderException) { // NOSONAR
				throw (ThrowableHolderException) e;
			}
			else {
				throw new RuntimeException(e);
			}
		}
		MessageBuilder<ResponseMsg> resultPayload = (MessageBuilder<ResponseMsg>)result;
		optionHandler = CustomCircuitBreakerByOption.getInstance();
		optionHandler.doInvoke(getOption(message.getPayload()), resultPayload.getPayload());
		
		return result;
	}
	
	private String getOption(Object payload) {
		return (String) payload;
	}

	private static class AdvisedMetadata {

		private final AtomicInteger failures = new AtomicInteger();
		private volatile long lastFailure;

		AdvisedMetadata() {
		}

		private long getLastFailure() {
			return this.lastFailure;
		}

		private void setLastFailure(long lastFailure) {
			this.lastFailure = lastFailure;
		}

		private AtomicInteger getFailures() {
			return this.failures;
		}

	}

	public static final class CircuitBreakerOpenException extends MessagingException {

		private static final long serialVersionUID = 1L;

		public CircuitBreakerOpenException(Message<?> message, String description) {
			super(message, description);
		}

	}

}
