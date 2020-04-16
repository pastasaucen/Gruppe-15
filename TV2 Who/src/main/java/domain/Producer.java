package domain;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Producer extends User implements IProducer {

	private UserType userType = UserType.PRODUCER;

	// The temporary production this producer creates before sending it to the ProductionCatalogue
	private Production production;

	private CastCatalog castCatalog; //so we dont need to create a CastCatalog more than once, value in contructor.

	private ProductionCatalog productionCatalog;

	public Producer(String name, String email) {
		super(name, email);
		castCatalog = CastCatalog.getInstance();
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
		// What about names like "Marie Louise Pedersen"? Or "Robert De Niro"?

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
		// TODO Is a part of the assign role use case. Will be implemented later
		production.addRole(roleName, castMember);
	}

	public void submitProduction() {
		production.setAssociatedProducerEmail(email);
		ProductionCatalog.getInstance().addProduction(production);
		production = null;
	}


	/**
	 * creates cast if not exist. if exist asks if still wants to create.
	 * @param firstName
	 * @param lastName
	 * @param email
	 */
	public void createCastMember(String firstName, String lastName, String email) {
		// TODO does it need to search for the cast members email?
		List<Cast> castList = castCatalog.searchForCast(firstName, lastName);


		if ( castList == null){
			createCastAndConfirms(firstName, lastName, email);
		} else{
			System.out.println("The cast you want to create might already exist");

			for(Cast cast : castList){
				System.out.println( cast.getFirstName() + " " + cast.getLastName() + " \n email: " + cast.getEmail());
				/*+ "\n roles:" + cast.getRoles().toString());
				 roles bliver ikke printet lige nu da vi ikke har arbjdet med Role klassen endnu */
			}

			System.out.println("Do you still want to create a new cast, type 'yes' or 'no' : ");

			Scanner scanner = new Scanner(System.in);
			if(scanner.next().equalsIgnoreCase("yes")){
				createCastAndConfirms(firstName, lastName, email);
			} else {
				System.out.println("You have chosen not to create the cast");

			}

		}

	}

	/**
	 * creates cast.
	 * @param firstName
	 * @param lastName
	 * @param email
	 */
	private void createCastAndConfirms(String firstName, String lastName, String email){
		castCatalog.createCastMember(firstName, lastName, email);
		System.out.println("The cast member:" + firstName + " " + lastName + " : " + email +
				" \n Has been created");
	}

	/**
	 * Searches for productions through productionCatalog by name or ID.
	 * If no productions is found, the producer is offered to create the production with the name of the input.
	 * @param nameOrID
	 */
	private void getProductions(String nameOrID) {
		if (productionCatalog.getProduction(nameOrID)) {

		}
		else {
			// Produceren får muligheden for at oprette productionen. -> Sendes til "opret produktion".

		}
	}



}


