package com.xzymon.p4avi.server.sb;

import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import com.xzymon.p4avi.server.model.Comment;

public interface CommentBase {
	List<Comment> getComments();
	String addComment(String message) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException;
}
