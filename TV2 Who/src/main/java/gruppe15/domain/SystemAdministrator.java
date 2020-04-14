package gruppe15.domain;

import Designklassediagram.ProducerInterface;

public class SystemAdministrator extends User, Producer implements ProducerInterface {

	public SystemAdministrator(String name, String username, String email) {

	}


	/**
	 * @see Designklassediagram.ProducerInterface#createProduction(String, Date)
	 */
	public void createProduction(String name, Date date) {

	}


	/**
	 * @see Designklassediagram.ProducerInterface#addCastMember(String)
	 * 
	 *  
	 */
	public void addCastMember(String name) {

	}


	/**
	 * @see Designklassediagram.ProducerInterface#addRole(String, Analyseklassediagram.Cast)
	 * 
	 *  
	 */
	public void addRole(String roleName, Cast castMember) {

	}


	/**
	 * @see Designklassediagram.ProducerInterface#submitProduction()
	 * 
	 *  
	 */
	public void submitProduction() {

	}


	/**
	 * @see Designklassediagram.ProducerInterface#createCastMember(String, String, boolean)
	 * 
	 *  
	 */
	public void createCastMember(String firstName, String lastName, boolean force) {

	}

}
