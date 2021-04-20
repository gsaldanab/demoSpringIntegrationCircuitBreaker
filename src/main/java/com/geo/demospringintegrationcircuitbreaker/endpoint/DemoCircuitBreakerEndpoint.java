package com.geo.demospringintegrationcircuitbreaker.endpoint;

import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.integration.handler.advice.RequestHandlerCircuitBreakerAdvice.CircuitBreakerOpenException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.geo.demospringintegrationcircuitbreaker.model.entity.ResponseDemo;
import com.geo.demospringintegrationcircuitbreaker.model.ws.HelloMsg;
import com.geo.demospringintegrationcircuitbreaker.model.ws.ResponseMsg;
import com.geo.demospringintegrationcircuitbreaker.util.ExceptionUtil;

@Component
public class DemoCircuitBreakerEndpoint {
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());

	public String getOption(Message<String> msg) {
		log.info("1.getOption");
		String option = msg.getPayload();
		return option;
	}

	public Message<?> buildResponse(ResponseMsg msg) {
		log.info("2.buildResponse");
		ResponseDemo returnMsg = new ResponseDemo();
		returnMsg.setOriginalMsg(msg.getResponseBody().getMsg());
		returnMsg.setMsg(String.format("code: %s, msg: %s", msg.getResponseStatus(), msg.getResponseBody().getMsg()));
		return MessageBuilder.withPayload(returnMsg)
				.setHeader("http_statusCode", HttpStatus.OK).build();
	}
	
	public Message<?> invocationErrorResponse(Exception ex) throws Exception{
		log.info("Exception: "+ex.getClass().getCanonicalName() + ", message: "+ex.getMessage());
		log.info("3.invocationErrorResponse");
		
//		if(ex instanceof CircuitBreakerOpenException || ExceptionUtil.getRootCause(ex) instanceof ConnectException) {
//			return circuitBreakerOpenHandlerException();
//		} 
//		throw ex;
		return circuitBreakerOpenHandlerException();
	}
	
	private Message<?> circuitBreakerOpenHandlerException(){
		log.info("4.circuitBreakerOpenHandlerException");
		ResponseDemo returnMsg = new ResponseDemo();
		returnMsg.setOriginalMsg("mensaje por defecto");
		returnMsg.setMsg("Ocurrio un error pero se muestra un mensaje por defecto y funciona bien");
		return MessageBuilder.withPayload(returnMsg)
				.setHeader("http_statusCode", HttpStatus.OK).build();
	}

}
