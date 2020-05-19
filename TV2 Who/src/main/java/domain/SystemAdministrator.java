package domain;

import domain.Cast;
import domain.User;
import domain.UserCatalog;
import domain.UserType;
import domain.editor.IEditor;
import domain.persistenceInterfaces.IPersistenceUser;
import domain.producer.IProducer;
import domain.rDUser.IRDUser;

import java.sql.Date;

public class SystemAdministrator extends User implements IProducer, IEditor, IRDUser {
	IPersistenceUser persistenceUser;
	UserCatalog userCatalog;

	public SystemAdministrator(String name, String email) {
		super(name, email, UserType.SYSTEMADMINISTRATOR);
		userCatalog = UserCatalog.getInstance();
	}


	/**
	 * Created a user depending on the type and sends it to the database.
	 * @param name
	 * @param email
	 * @param userType Enum
	 * @param password password is deleted after sending to Database
	 */
	public void createUser(String name, String email, UserType userType, String password){
		userCatalog.createUser(name, email, userType, password);
	}

	/**
	 */
	public void createProduction(String name, Date date)  {

	}

	/**
	 * 
	 *  
	 */
	public void addCastMember(String name) {

	}

	@Override
	public void addCastMember(Cast cast, Production production) {

	}

	/**
	 * 
	 *  
	 */
	public void addRole(String roleName, Cast castMember) {

	}

	@Override
	public void addRole(String roleName, Cast castMember, Production production) {

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
	public void createCastMember(String firstName, String lastName, String email, String bio) {

	}

}
