package gruppe15.domain;

import java.util.ArrayList;
import java.util.List;

public class CastCatalog {

	private static CastCatalog instance;

	//private Cast[] cast; // Ved ikke helt hvorfor dette er et array, så vi hare ikke brugt det

	private ArrayList<Cast> castArrayList = new ArrayList<>();

	int numberOfCastMembers = 0; // makes sure that no one has the same id.


	public static CastCatalog getInstance() {
		if ( instance == null){
			instance = new CastCatalog();
		}
		return instance;
	}

	/**
	 *
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return
	 */
	public List<Cast> searchForCast(String firstName, String lastName, String email) {
// vi har lavet ArrayList for nu, indtil det bliver implementeret.
		ArrayList<Cast> castList = new ArrayList<>();
		return castList;
	}

	/**
	 * creates a cast member and adds it to the database (for now arraylist)
	 * @param firstName
	 * @param lastName
	 * @param email
	 */
	public void createCastMember(String firstName, String lastName, String email) {
		numberOfCastMembers ++;
		castArrayList.add(new Cast(numberOfCastMembers, firstName, lastName, email));
	}

}
