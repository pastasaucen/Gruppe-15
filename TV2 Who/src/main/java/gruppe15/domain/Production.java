package gruppe15.domain;

import java.sql.Date;

public class Production {

	private int id;

	private String name;

	private Date releaseDate;

	private State state = State.PENDING;

	private String associatedProducerUsername;

	private Cast[] cast;

	public Production(int id, String name, Date releaseDate) {

	}

	public void addCastMember(Cast castMember) {

	}

	public void addRole(String roleName, Cast cast) {

	}

}
