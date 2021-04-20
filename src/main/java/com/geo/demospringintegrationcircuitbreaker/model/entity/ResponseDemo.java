package com.geo.demospringintegrationcircuitbreaker.model.entity;

public class ResponseDemo {
	private String originalMsg;
	private String msg;


	public ResponseDemo() {
		
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getOriginalMsg() {
		return originalMsg;
	}

	public void setOriginalMsg(String originalMsg) {
		this.originalMsg = originalMsg;
	}
}
