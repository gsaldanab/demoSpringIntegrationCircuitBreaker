package com.geo.demospringintegrationcircuitbreaker.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geo.demospringintegrationcircuitbreaker.exception.OwnException;
import com.geo.demospringintegrationcircuitbreaker.model.ws.ResponseMsg;

public class CustomCircuitBreakerByOption {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static CustomCircuitBreakerByOption customCircuitBreakerByOption;
	
	public static CustomCircuitBreakerByOption getInstance() {
		if(customCircuitBreakerByOption == null) {
			customCircuitBreakerByOption = new CustomCircuitBreakerByOption();
		}
		return customCircuitBreakerByOption;
	}
	
	private int threshold = 1;
	private long halfOpenAfter = 120000l;
	
	private final ConcurrentMap<String, OptionMetadata> metadataMap = new ConcurrentHashMap<>();
	
	public Object doInvoke(String option, ResponseMsg response) {
		OptionMetadata metadata = this.metadataMap.get(option);
		if (metadata == null) {
			this.metadataMap.putIfAbsent(option, new OptionMetadata());
			metadata = this.metadataMap.get(option);
		}
		if (metadata.getFailures().get() >= this.threshold &&
				System.currentTimeMillis() - metadata.getLastFailure() < this.halfOpenAfter) {
			throw new OwnException("Option Circuit Breaker is Open for option " + option);
		}
		
		if(response.getResponseStatus() == 0){
			if (logger.isDebugEnabled() && metadata.getFailures().get() > 0) {
				logger.debug("Option Closing Circuit Breaker for option" + option);
			}
			metadata.getFailures().set(0);
		}else{
			metadata.getFailures().incrementAndGet();
			metadata.setLastFailure(System.currentTimeMillis());
		}
		return response;
	}

	private static class OptionMetadata {

		private final AtomicInteger failures = new AtomicInteger();
		private volatile long lastFailure;

		OptionMetadata() {
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
}
