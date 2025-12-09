package com.lexivo.schema;

public class AuthReqBody {
	private String name;
	private String email;
	private String passwordHash;
	private boolean confirmed;
	private String password;
	private String adminPassword;
	private String newPassword;
	private String refreshToken;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "AuthReqBody{" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				", confirmed=" + confirmed +
				", isPasswordNullOrBlank='" + (password == null || password.isBlank()) + '\'' +
				", isAdminPasswordIsNullOrBlank='" + (adminPassword == null || adminPassword.isBlank()) + '\'' +
				", isNewPasswordNullOrBlank='" + (newPassword == null || newPassword.isBlank()) + '\'' +
				", isRefreshTokenNullOrBlank='" + (refreshToken == null || refreshToken.isBlank()) + '\'' +
				'}';
	}
}
