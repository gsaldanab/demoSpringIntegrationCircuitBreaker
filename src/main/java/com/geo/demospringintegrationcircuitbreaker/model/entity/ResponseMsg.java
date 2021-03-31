package com.geo.demospringintegrationcircuitbreaker.model.entity;

public class ResponseMsg {
	private String originalMsg;
	private String msg;


	public ResponseMsg() {
		
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
