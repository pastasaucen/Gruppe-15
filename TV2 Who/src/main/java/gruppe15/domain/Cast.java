package gruppe15.domain;

import java.util.Arrays;

public class Cast {

	private int id;

	private String firstName;

	private String lastName;

	private Role[] roles;

	public Cast(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public void addRole(String roleName, Production production) {

	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public String toString() {
		return "Cast{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", roles=" + Arrays.toString(roles) +
				'}';
	}
}
