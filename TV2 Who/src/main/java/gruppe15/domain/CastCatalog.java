package gruppe15.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CastCatalog {

	private CastCatalog instance;

	//private Cast[] cast;

	private ArrayList<Cast> castArrayList = new ArrayList<>();

	int numberOfCastMembers = 0; // makes sure that no one ha the same id.

	public CastCatalog() {

	}

	public static CastCatalog getInstance() {
		return null;
	}

	public List<Cast> searchForCast(String firstName, String lastName, String email) {
		return null;
	}


	public void createCastMember(String firstName, String lastName, String email) {
		numberOfCastMembers ++;
		castArrayList.add(new Cast(numberOfCastMembers, firstName, lastName, email));
	}

}
