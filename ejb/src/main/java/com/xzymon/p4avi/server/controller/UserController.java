package com.xzymon.p4avi.server.controller;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;

import com.xzymon.p4avi.server.sb.UserLocal;

@Model
public class UserController {
	@EJB
	private UserLocal userLocal;
	
	public String invokeBean4EMF(){
		userLocal.getUsers();
		return "UserLocal invoked";
	}
}
