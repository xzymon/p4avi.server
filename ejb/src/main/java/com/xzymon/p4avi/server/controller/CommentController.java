package com.xzymon.p4avi.server.controller;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;

import com.xzymon.p4avi.server.sb.CommentLocal;

@Model
public class CommentController {
	@EJB
	private CommentLocal commentLocal;
	
	public String invokeBean4EMF(){
		commentLocal.getComments();
		return "commentLocal invoked";
	}
}
