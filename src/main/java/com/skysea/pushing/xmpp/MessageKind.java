package com.skysea.pushing.xmpp;

public enum MessageKind {
	ARTICLE("article");
	
	
	private String type;
	private MessageKind(String type){
		this.type=type;
	}
	public String getType(){
		return type;
	}
}
