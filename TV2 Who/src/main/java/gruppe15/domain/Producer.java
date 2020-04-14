package gruppe15.domain;

import java.sql.Date;
import java.util.List;

public class Producer extends User implements IProducer {

	// The temporary production this producer creates before sending it to the ProductionCatalogue
	private Production production;

	private CastCatalog castCatalog;

	private ProductionCatalog productionCatalog;

	public Producer(String name, String username, String email) {
		super(name, username, email);
	}

	/**
	 * Creates a production instance with the id of -1.
	 * @param name
	 * @param date
	 */
	@Override
	public void createProduction(String name, Date date) {
		production = new Production(-1, name, date);
	}


	public void addCastMember(String name) {
		// We assume that the user will search in one string. I.e. want to separate this string into to names.

		String[] names = name.split(" ");		// Divides the given name into separate names as an array
		String firstName = "";

		// Minus one to leave the last name as the last name.
		for (int nameNum = 0; nameNum < names.length - 1; nameNum++) {
			// Adds all the first names (also the middle names)
			if (nameNum == 0) {		// If it is the first name, then don't add a space in between.
				firstName += names[nameNum];
			} else {
				firstName += " " + names[nameNum];
			}
		}

		// Adds the last string in the names array as the last name.
		String lastName = names[names.length-1];

		// Retrieves the relevant castMembers with the given names
		List<Cast> relevantCastMembers = CastCatalog.getInstance().searchForCast(firstName, lastName);

		if (relevantCastMembers.size() > 1) {
			// TODO We have not decided what to do here!!!
			System.out.println("There are more than one cast member with that name! We haven't decided what to do here!");
			return;
		} else if (relevantCastMembers.size() == 0) {
			// If no one was found with the given name
			System.out.println("No cast member was found with the name \"" + name + "\"...");
			return;
		}

		Cast castMember = relevantCastMembers.get(0);

		production.addCastMember(castMember);	// Adds the only cast member to the new production.
		System.out.println("The cast member named \""+castMember.getFirstName() +" "+ castMember.getLastName()+"\"" +
				" is added to the production \""+production.getName()+"\"");
	}

	public void addRole(String roleName, Cast castMember) {

	}

	public void submitProduction() {

	}

	public void createCastMember(String firstName, String lastName, boolean force) {

	}

}
