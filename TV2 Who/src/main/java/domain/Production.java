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
	private String tvCode;

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

	public Production(String name, Date releaseDate) {
		this.name = name;
		this.releaseDate = releaseDate;
	}

	public Production(int id, String name, Date releaseDate, State state, String tvCode, String associatedProducerEmail) {
		this.id = id;
		this.name = name;
		this.releaseDate = releaseDate;
		this.state = state;
		this.tvCode = tvCode;
		this.associatedProducerEmail = associatedProducerEmail;
	}

	public void addCastMember(Cast castMember) {
		cast.add(castMember);
}

	public void addRole(String roleName, Cast castMember) {
		// Iterates through every cast member in this production
		for (int castNum = 0; castNum < cast.size(); castNum++) {
			// If the cast member is the same as the given cast member, then add the role to that one.
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

	public void setId(int id) {
		this.id = id;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public State getState() {
		return state;
	}

	public String getAssociatedProducerEmail() {
		return associatedProducerEmail;
	}

	public List<Cast> getCast() {
		return cast;
	}

	public void setAssociatedProducerEmail(String associatedProducerEmail) {
		this.associatedProducerEmail = associatedProducerEmail;
	}

	public String getTvCode() {
		return tvCode;
	}

	@Override
	public String toString() {
		String castString = "";

		for (int i = 0; i < cast.size(); i++) {
			castString += '\t' + (cast.get(i).toString() + '\n');
		}

		return "Production:\n" +
				"id=" + id + '\n' +
				"name ='" + name + '\'' + '\n' +
				"releaseDate =" + releaseDate + '\n' +
				"state =" + state + '\n' +
				"associatedProducerEmail ='" + associatedProducerEmail + '\'' + '\n' +
				"cast =" + "\n" + castString;
	}
}
