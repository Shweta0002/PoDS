package in.ac.iisc.users.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.*;

@Getter
@Setter
@Entity
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdGen")
	@SequenceGenerator(name = "userIdGen", initialValue = 1, allocationSize = 1)
	Integer userId;

	String name;

	@Column(unique = true)
	String email;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

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

	public Users(Integer userId, String name, String email) {
		super();
		this.userId = userId;
		this.name = name;
		this.email = email;
	}

	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}
}
