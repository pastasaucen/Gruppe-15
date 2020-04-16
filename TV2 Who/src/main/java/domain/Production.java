package domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Production {

	private int id;

	private String name;

	private Date releaseDate;

	private State state = State.PENDING;

	private String associatedProducerEmail;

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

	public Production(int id, String name, Date releaseDate, State state, String associatedProducerEmail) {
		this.id = id;
		this.name = name;
		this.releaseDate = releaseDate;
		this.state = state;
		this.associatedProducerEmail = associatedProducerEmail;
	}

	public void addCastMember(Cast castMember) {
		cast.add(castMember);
}

	public void addRole(String roleName, Cast castMember) {
		for (int castNum = 0; castNum < cast.size(); castNum++) {
			if (castMember.equals(cast.get(castNum))) {
				cast.get(castNum).addRole(roleName, this);
				return;
			}
		}
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Production{" +
				"id=" + id +
				", name ='" + name + '\'' +
				", releaseDate =" + releaseDate +
				", state =" + state +
				", associatedProducerEmail ='" + associatedProducerEmail + '\'' +
				", cast =" + cast +
				'}';
	}

	public void setAssociatedProducerEmail(String associatedProducerEmail) {
		this.associatedProducerEmail = associatedProducerEmail;
	}
}
