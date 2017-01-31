package com.kit.http;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * User account model
 *
 */
public final class UserAccount {

	public enum Type {
		FREE_USER, DEVELOPER
	}

	public enum Status {
		ACTIVE, ACTIVATION_PENDING, DISABLED
	}

	@JsonProperty("id")
	private long id;
	@JsonProperty("type")
	private Type type;
	@JsonProperty("status")
	private Status status;
	@JsonProperty("email")
	private String email;
	@JsonProperty("password")
	private String password;
	@JsonProperty("created")
	private Timestamp created;
	@JsonProperty("updated")
	private Timestamp updated;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return "UserAccount{" +
				"id=" + id +
				", type=" + type +
				", status=" + status +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", created=" + created +
				", updated=" + updated +
				'}';
	}
}
