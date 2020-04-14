package gruppe15.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Production {

	private int id;

	private String name;

	private Date releaseDate;

	private State state = State.PENDING;

	private String associatedProducerUsername;

	private List<Cast> cast = new ArrayList<>();

	/**
	 * Creates a production instance.
	 * @param id the id of the production from the database.
	 * @param name the name of the new production
	 * @param releaseDate the release date
	 */
	public Production(int id, String name, Date releaseDate) {
		this.id = id;
		this.name = name;
		this.releaseDate = releaseDate;
	}

	public void addCastMember(Cast castMember) {
		cast.add(castMember);
	}

	public void addRole(String roleName, Cast cast) {

	}

	public String getName() {
		return name;
	}
}
