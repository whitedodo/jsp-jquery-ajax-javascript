package com.example.web.model;

public class CompUsers {

	private String username;
	private String password;
	private int enabled;
	
	public CompUsers(String username, String password, int enabled) {
		
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		
	}

	public CompUsers() {
		
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getEnabled() {
		return enabled;
	}
	
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	
	
}
