package gruppe15.domain;

import java.util.ArrayList;
import java.util.List;

public class CastCatalog {

	private static CastCatalog instance;

	// Temporary until the persistence layer is implemented. We always want the newest data from the persistence layer.
	private List<Cast> cast = new ArrayList<>();

	private CastCatalog() {
		cast.add(new Cast(-1, "Bob", "Bobsen"));
		cast.add(new Cast(-1, "Karen", "Bobsen"));
		cast.add(new Cast(-1, "Bob", "Sørensen"));
	}

	public static CastCatalog getInstance() {
		if (instance == null) {
			instance = new CastCatalog();
		}

		return instance;
	}

	/**
	 * Finds the cast member with the given first name and last name.
	 * @param firstName
	 * @param lastName
	 * @return a list of cast members with the given names
	 */
	public List<Cast> searchForCast(String firstName, String lastName) {
		// TODO When the persistence layer is implemented, use this to search for the correct cast member.

		List<Cast> relevantCastMembers = new ArrayList<>();

		for (int castNum = 0; castNum < cast.size(); castNum++) {
			Cast curCast = cast.get(castNum);

			// Do the current cast member have the same first and last name as the search words?
			if (curCast.getFirstName().equals(firstName) && curCast.getLastName().equals(lastName)) {
				relevantCastMembers.add(curCast);		// Adds this member to the return list
			}
		}

		return relevantCastMembers;
	}

	public void createCastMember(String firstName, String lastName, boolean force) {

	}

}
