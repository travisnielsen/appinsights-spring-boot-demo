package com.usermanagement.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

	private String firstName;
	
	private String lastName;
	
	private Timestamp createdAt;

	private Timestamp updatedAt;

	public User() {

	}

	public User(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.createdAt = new Timestamp(System.currentTimeMillis());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt() {
		this.createdAt = new Timestamp(System.currentTimeMillis());
	}

	public Timestamp getLastModified() {
		return updatedAt;
	}

	public void setLastModified(Timestamp lastModified) {
		this.updatedAt = lastModified;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		this.updatedAt = new Timestamp(System.currentTimeMillis());
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		this.updatedAt = new Timestamp(System.currentTimeMillis());
	}

	@Override
	public String toString() {
		return "User {" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(id, user.id) &&
				Objects.equals(firstName, user.firstName) &&
				Objects.equals(lastName, user.lastName) &&
				Objects.equals(createdAt, user.createdAt) &&
				Objects.equals(updatedAt, user.updatedAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, createdAt, updatedAt);
	}

}