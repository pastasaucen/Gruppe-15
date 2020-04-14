package gruppe15.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cast {

	private int id;

	private String firstName;

	private String lastName;

	private List<Role> roles = new ArrayList<>();

	private String email;

	public Cast(int id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public void addRole(String roleName, Production production) {
		roles.add(new Role(roleName, production));
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}
	
	public boolean equals(Cast cast) {
		if (id == -1 || cast.id == -1){ // If any of them have -1 as ID, they haven't got an ID yet and therefore we check on name.
			if (firstName.equals(cast.firstName) && lastName.equals(cast.lastName)) {
				return true;
			} else {
				return false;
			}
		} else if (id == cast.id) { // If both have a valid ID, we compare ID's.
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Cast{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", roles=" + roles +
				'}';
	}
}
