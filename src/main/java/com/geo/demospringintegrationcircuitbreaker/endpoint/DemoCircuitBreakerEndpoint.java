package com.geo.demospringintegrationcircuitbreaker.endpoint;

import java.net.ConnectException;

import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.integration.handler.advice.RequestHandlerCircuitBreakerAdvice.CircuitBreakerOpenException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.geo.demospringintegrationcircuitbreaker.model.entity.ResponseMsg;
import com.geo.demospringintegrationcircuitbreaker.model.ws.HelloMsg;
import com.geo.demospringintegrationcircuitbreaker.util.ExceptionUtil;

@Component
public class DemoCircuitBreakerEndpoint {
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());

	public String getName(Message<String> msg) {
		log.info("1.getName");
		String name = msg.getPayload();
		return name + ":1";
	}

	public Message<?> buildResponse(HelloMsg msg) {
		log.info("2.buildResponse");
		ResponseMsg returnMsg = new ResponseMsg();
		returnMsg.setOriginalMsg(msg.getMsg());
		returnMsg.setMsg(msg.getMsg() + ", time is " + msg.getCurrentTime());
		return MessageBuilder.withPayload(returnMsg)
				.setHeader("http_statusCode", HttpStatus.OK).build();
	}
	
	public Message<?> invocationErrorResponse(Exception ex) throws Exception{
		log.info("Exception: "+ex.getClass().getCanonicalName() + ", message: "+ex.getMessage());
		log.info("3.invocationErrorResponse");
		
		if(ex instanceof CircuitBreakerOpenException || ExceptionUtil.getRootCause(ex) instanceof ConnectException) {
			return circuitBreakerOpenHandlerException();
		} 
		throw ex;
	}
	
	private Message<?> circuitBreakerOpenHandlerException(){
		log.info("4.circuitBreakerOpenHandlerException");
		ResponseMsg returnMsg = new ResponseMsg();
		returnMsg.setOriginalMsg("mensaje por defecto");
		returnMsg.setMsg("Ocurrio un error pero se muestra un mensaje por defecto y funciona bien");
		return MessageBuilder.withPayload(returnMsg)
				.setHeader("http_statusCode", HttpStatus.OK).build();
	}

}
