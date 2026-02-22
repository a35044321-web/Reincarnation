package model;

import java.time.LocalDateTime;

public class Users {
	private int users_id;
	private String account;
	private String password;
	private String name;    // 💡 新增
	private String phone;   // 💡 新增
	private String email;   // 💡 新增
	private LocalDateTime created_at;
	private String role; // 🚀 新增屬性
	
	
	public Users() {
		super();
	}
	
	public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getUsers_id() {
		return users_id;
	}
	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
		
}
