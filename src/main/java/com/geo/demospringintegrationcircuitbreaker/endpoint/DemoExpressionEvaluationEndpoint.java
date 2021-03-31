package com.geo.demospringintegrationcircuitbreaker.endpoint;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.integration.handler.advice.ExpressionEvaluatingRequestHandlerAdvice.MessageHandlingExpressionEvaluatingAdviceException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.geo.demospringintegrationcircuitbreaker.model.entity.ResponseMsg;
import com.geo.demospringintegrationcircuitbreaker.model.ws.HelloMsg;

@Component
public class DemoExpressionEvaluationEndpoint {
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());

	public String getName(Message<String> msg) {
		String name = msg.getPayload();
		log.info("Nombre:" + name);
		return name + ":1";
	}

	public String buildWsRequest(String name) {
		String newName = name.concat(":2");
		log.info("Request para servicio externo:" + newName);
		return newName;
	}

	public String getWsResponse(HelloMsg msg) {
		String message = msg.getMsg();
		String currentTime = msg.getCurrentTime();
		log.info("Response obtenido: " + msg);
		String strMsg = message.concat(" today is:").concat(currentTime).concat(":3");
		return strMsg;
	}


	public Message<?> getErrorResponse(MessageHandlingExpressionEvaluatingAdviceException exepcion) {
		String message = "mensaje de error por circuitBreaker";
		String currentTime = "";
		log.info("Response obtenido: " + message);
		String strMsg = message.concat(" today is:").concat(currentTime).concat(":3");
		return MessageBuilder.withPayload(strMsg)
				.setHeader("http_statusCode", HttpStatus.OK).build();
	}
	
	public Message<?> buildResponse(String msg) {
		msg += ":4";
		ResponseMsg returnMsg = new ResponseMsg();
		returnMsg.setOriginalMsg(msg);
		
		log.info("Mensaje final: " + msg);
		return MessageBuilder.withPayload(returnMsg)
				.setHeader("http_statusCode", HttpStatus.OK).build();
	}
	
}
