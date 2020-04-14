package gruppe15.domain;

import java.sql.Date;

public class SystemAdministrator extends User implements IProducer {

	public SystemAdministrator(String name, String email) {
		super(name, email);
	}


	/**
	 */
	public void createProduction(String name, Date date) {

	}

	/**
	 * 
	 *  
	 */
	public void addCastMember(String name) {

	}


	/**
	 * 
	 *  
	 */
	public void addRole(String roleName, Cast castMember) {

	}


	/**
	 * 
	 *  
	 */
	public void submitProduction() {

	}


	/**
	 * 
	 *  
	 */
	public void createCastMember(String firstName, String lastName, String email) {

	}

}
