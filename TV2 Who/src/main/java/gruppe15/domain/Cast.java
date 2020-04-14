package gruppe15.domain;

import java.util.UUID;

public class Cast {

	private String firstName;

	private String lastName;

	private int id;

	private String email;


	private Role[] roles; // roles skal laves om alt efter Role. Måske til arrayList når vi ikke arbejder med Database endnu

	public Cast( int id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public void addRole(String roleName, Production production) {

	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

}
