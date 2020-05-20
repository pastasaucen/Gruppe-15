package domain.producer;

import domain.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class Producer extends User implements IProducer {

	// The temporary production this producer creates before sending it to the ProductionCatalogue
	private Production tempProduction;

	private CastCatalog castCatalog; // Stores the instance reference that is retrieved in the constructor.

	// This is not used, but can be used later to only get productions this producer has made
	private ProductionCatalog productionCatalog; // Stores the instance reference that is retrieved in the constructor.

	public Producer(String name, String email) {
		super(name, email, UserType.PRODUCER);
		castCatalog = CastCatalog.getInstance();
		productionCatalog = ProductionCatalog.getInstance();
	}

	/**
	 * Creates a production instance with the id of -1.
	 * @param name
	 * @param date
	 */
	@Override
	public void createProduction(String name, Date date) {
		tempProduction = new Production(-1, name, date);
	}

	@Override
	public List<Production> getAssociatedProductions() {
		return productionCatalog.getProductions(this);
	}

	/**
	 * This method is used during the creation of a new production.
	 * @param castMember
	 */
	public void addCastMember(Cast castMember) {

		tempProduction.addCastMember(castMember);	// Adds the only cast member to the new production.
		System.out.println("The cast member named \""+castMember.getFirstName() +" "+ castMember.getLastName()+"\"" +
				" is added to the production \""+ tempProduction.getName()+"\"");
	}

	/**
	 * Used to add a cast member to an existing production.
	 * @param cast
	 * @param production
	 */
	public void addCastMember(Cast cast, Production production) {
		production.addCastMember(cast);
		ProductionCatalog.getInstance().assignCastMemberToProduction(cast, production);
		ProductionCatalog.getInstance().addProduction(production);
	}

	// Adds role to castMember on TEMPORARY production
	public void addRole(String roleName, Cast castMember) {
		tempProduction.addRole(roleName, castMember);
	}

	// Adds role to castMember on SPECIFIC production
	public void addRole(String roleName, Cast castMember, Production production) {
		production.addRole(roleName, castMember);
		castCatalog.saveCastMembers(Arrays.asList(castMember));
	}

	/**
	 * This method is only used when creating a production from scratch.
	 */
	public void submitProduction() {
		tempProduction.setAssociatedProducerEmail(email);
		ProductionCatalog.getInstance().addProduction(tempProduction);

		List<Cast> castList = tempProduction.getCastList();
		for (int i = 0; i < castList.size(); i++) {
			ProductionCatalog.getInstance().assignCastMemberToProduction(castList.get(i), tempProduction);
		}

		tempProduction = null;
	}

	/**
	 * Creates cast if not exist. if exist asks if still wants to create.
	 * @param firstName
	 * @param lastName
	 * @param email
	 */
	public void createCastMember(String firstName, String lastName, String email, String bio) {
		// TODO does it need to search for the cast members email?
		List<Cast> castList = castCatalog.searchForCast(email, this);

		// Checks whether the retrieved cast list is null (empty). If so then no duplicates were found.
		if (castList.size() == 0) {
			createCastAndConfirms(firstName, lastName, email, bio);	// Creates the new cast member
		}
	}

	/**
	 * Creates cast member after the user has confirmed the creation of a duplicate.
	 * @param firstName
	 * @param lastName
	 * @param email
	 */
	private void createCastAndConfirms(String firstName, String lastName, String email, String bio) {
		System.out.println("The cast member:\"" + firstName + " " + lastName + " : " + email + "\" has been created");
		castCatalog.createCastMember(firstName, lastName, email, bio);
	}

	public Production getTempProduction() {
		return tempProduction;
	}

	public String getEmail(){
		return this.email;
	}

}


