package com.xzymon.p4avi.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name="UZYTK",schema="aviprod")
public class User {
	@Id
	@GeneratedValue
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="NAZWISKO")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", version=" + version + ", name=" + name + "]";
	}
}
