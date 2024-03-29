package com.demo.pojo;

// Generated 2012-4-2 21:29:41 by Hibernate Tools 3.4.0.CR1

/**
 * User generated by hbm2java
 */
public class User implements java.io.Serializable {

	private long id;
	private String username;
	private String password;
	private Integer genderId;

	public User() {
	}

	public User(long id) {
		this.id = id;
	}

	public User(long id, String username, String password, Integer genderId) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.genderId = genderId;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getGenderId() {
		return this.genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

}
