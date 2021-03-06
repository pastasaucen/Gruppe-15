package domain;

import java.util.ArrayList;
import java.util.List;

public class Cast {

	private int id;

	private String firstName, lastName;

	private List<Role> roles = new ArrayList<>();

	private String email;

	private String bio;

	public Cast(int id, String firstName, String lastName, String email, String bio) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.bio = bio;

	}

	public void addRole(int id, String roleName, Production production) {
		roles.add(new Role(id, roleName, production));
	}

	// Runs update method to persistence-layer, with specific values
	// SQL-draft: UPDATE roleTable SET role = roleName WHERE id = roleID AND production = prodution.getID();
	/*public void updateRole(String roleName, int roleID) {
		Production production = roles.get(roleID).getProduction();
		roles.set(roleID, new Role(roleID, roleName, production));
	}*/

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public String getBio() {
		return bio;
	}

	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * Compares them by their ID if both of them contain valid IDs. If not then they are compared by their name.
	 * @param cast the cast member to compare with.
	 * @return
	 */
	public boolean equals(Cast cast) {
        // If both have a valid ID, we compare ID's.
        if (id == -1 || cast.id == -1) {
			// If any of them have -1 as ID, they haven't got an ID yet and therefore we check on name.
            return firstName.equals(cast.firstName) && lastName.equals(cast.lastName);

		} else return id == cast.id;

    }

	@Override
	public String toString() {
		return  firstName + ' ' + lastName + '\n' +
				" * Bio: " + bio + '\n' +
				" * Roles: " + roles;
	}
}
