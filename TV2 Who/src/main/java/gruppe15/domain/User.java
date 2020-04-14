package gruppe15.domain;

public abstract class User {

	protected String name, email;

	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}
}
