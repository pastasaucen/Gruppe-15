package domain;

import domain.editor.IEditor;
import domain.producer.IProducer;
import domain.producer.Producer;
import domain.rDUser.IRDUser;

import java.sql.Date;
import java.util.List;

public class SystemAdministrator extends User implements IProducer, IEditor, IRDUser {

	UserCatalog userCatalog;
	IProducer producerRole;

	public SystemAdministrator(String name, String email) {
		super(name, email, UserType.SYSTEMADMINISTRATOR);
		userCatalog = UserCatalog.getInstance();
		producerRole = new Producer(super.name, super.email);
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

	@Override

	public void createProduction(String name, Date date)  {
		producerRole.createProduction(name, date);
	}

	@Override
	public List<Production> getAssociatedProductions() {
		return producerRole.getAssociatedProductions();
	}

	@Override
	public void addCastMember(Cast castMember) {
		producerRole.addCastMember(castMember);
	}

	@Override
	public void addCastMember(Cast cast, Production production) {
		producerRole.addCastMember(cast, production);
	}

	@Override
	public void addRole(String roleName, Cast castMember) {
		producerRole.addRole(roleName, castMember);
	}

	@Override
	public void addRole(String roleName, Cast castMember, Production production) {
		producerRole.addRole(roleName, castMember, production);
	}

	@Override
	public void submitProduction() {
		producerRole.submitProduction();
	}

	@Override
	public void createCastMember(String firstName, String lastName, String email, String bio) {
		producerRole.createCastMember(firstName, lastName, email, bio);
	}

	@Override
	public Production getTempProduction() {
		return producerRole.getTempProduction();
	}
}
