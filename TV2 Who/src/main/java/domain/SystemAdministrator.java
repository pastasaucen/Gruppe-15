package domain;

import java.sql.Date;

public class SystemAdministrator extends User implements IProducer, ISystemAdministrator, IEditor, IRDUser {
	IPersistenceUser persistenceUser;
	UserCatalog userCatalog;

	private UserType userType = UserType.SYSTEMADMINISTRATOR;

	public SystemAdministrator(String name, String email) {
		super(name, email);
		userCatalog = UserCatalog.getInstance();
	}


	/**
	 * Created a user depending on the type and sends it to the database.
	 * @param name
	 * @param email
	 * @param userType Enum
	 * @param password pasword is deleted after sending to Database
	 */
	public void createUser(String name, String email, UserType userType, String password){
		userCatalog.createUser(name, email, userType, password);
		password = null;
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
