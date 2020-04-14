package gruppe15.domain;


import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Producer extends User implements ProducerInterface {

	private Production production;

	private CastCatalog castCatalog; //so we dont need to create a CastCatalog more than once, value in contructor.

	private ProductionCatalog productionCatalog;


	public Producer(String name, String username, String email) {
		castCatalog = CastCatalog.getInstance();
	}

	@Override
	public void createProduction(String name, Date date) {
	}

	public void addCastMember(String name) {
	}

	public void addRole(String roleName, Cast castMember) {

	}

	public void submitProduction() {

	}


	/**
	 * creates cast if not exist. if exist asks if still wants to create.
	 * @param firstName
	 * @param lastName
	 * @param email
	 */
	public void createCastMember(String firstName, String lastName,String email) {
		List<Cast> castList = castCatalog.searchForCast(firstName, lastName, email);


		if ( castList == null){
			createCastAndPrompt(firstName, lastName, email);
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
				createCastAndPrompt(firstName, lastName, email);
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
	private void createCastAndPrompt(String firstName, String lastName, String email){
		castCatalog.createCastMember(firstName, lastName, email);
		System.out.println("The cast member:" + firstName + " " + lastName + " : " + email +
				" \n Has been created");
	}

}


