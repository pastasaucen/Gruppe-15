package domain;

public abstract class User {

	protected String name, email;
	protected UserType userType;

	public User(String name, String email, UserType userType) {
		this.name = name;
		this.email = email;
		this.userType = userType;
	}

	public String getName() {
		return name;
	}

	public UserType getUserType() {
		return userType;
	}

	public String getEmail() {
		return email;
	}
}
