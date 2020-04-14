package gruppe15.domain;

import java.util.ArrayList;
import java.util.List;

public class CastCatalog {

	private static CastCatalog instance;

	private List<Cast> cast	 = new ArrayList<>();

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
		return null;
	}

	/**
	 * creates a cast member and adds it to the database (for now arraylist)
	 * @param firstName
	 * @param lastName
	 * @param email
	 */
	public void createCastMember(String firstName, String lastName, String email) {
		numberOfCastMembers ++;
		cast.add(new Cast(numberOfCastMembers, firstName, lastName, email));
	}

}
