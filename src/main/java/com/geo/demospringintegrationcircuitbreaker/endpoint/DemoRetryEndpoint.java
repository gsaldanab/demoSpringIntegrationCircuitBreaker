package com.geo.demospringintegrationcircuitbreaker.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.geo.demospringintegrationcircuitbreaker.model.entity.ResponseMsg;
import com.geo.demospringintegrationcircuitbreaker.model.ws.HelloMsg;

@Component
public class DemoRetryEndpoint {
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
	
	public Message<?> invocationErrorResponse(MessagingException ex) throws Exception{
		log.info("Exception: "+ex.getClass().getCanonicalName() + ", message: "+ex.getMessage());
		log.info("3.invocationErrorResponse");
		
		
		return MessageBuilder.withPayload(ex.getFailedMessage())
				.setHeader("http_statusCode", HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}
