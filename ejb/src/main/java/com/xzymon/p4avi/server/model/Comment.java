package com.xzymon.p4avi.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="KOMENTARZ", schema="aviextr")
public class Comment {
	@Id
	@GeneratedValue
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="MESSAGE")
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
