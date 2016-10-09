package com.xzymon.p4avi.server.sb;

import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import com.xzymon.p4avi.server.model.User;

public interface UserBase {
	List<User> getUsers();
	String addUser(String name);
}
