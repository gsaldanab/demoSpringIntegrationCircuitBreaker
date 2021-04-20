package com.geo.demospringintegrationcircuitbreaker.model.ws;

public class ResponseMsg {

	private int responseStatus;
	private HelloMsg responseBody;
	
	
	public ResponseMsg() {
		
	}
	
	public ResponseMsg(int responseStatus) {
		this.responseStatus = responseStatus;
	}
	
	public ResponseMsg(int responseStatus, HelloMsg responseBody) {
		this.responseStatus = responseStatus;
		this.responseBody = responseBody;
	}

	public int getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
	}

	public HelloMsg getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(HelloMsg responseBody) {
		this.responseBody = responseBody;
	}
	
	
	
}
