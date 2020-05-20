package domain;

import domain.producer.Producer;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class Production {

	private int id;
	private String name;
	private Date releaseDate;
	private State state = State.PENDING;
	private String associatedProducerEmail;
	private List<Cast> castList = new ArrayList<>();
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
		castList.add(castMember);
}

	// Returns updated list, after adding a role to a cast.
	public void addRole(String roleName, Cast castMember) {
		castMember.addRole(-1, roleName, this);
	}

	// Parses values to be updated to Cast
	/*public void updateRole(String roleName, int roleID, Cast castMember) {
		castMember.updateRole(roleName, roleID);
	}*/

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

	public List<Cast> getCastList() {
		return castList;
	}

	public void setAssociatedProducerEmail(String associatedProducerEmail) {
		this.associatedProducerEmail = associatedProducerEmail;
	}

	public String getTvCode() {
		return tvCode;
	}

	public void setTvCode(String tvCode) {
		this.tvCode = tvCode;
	}

	@Override
	public String toString() {
		String castString = "";

		for (Cast value : castList) {
			castString += "- " +(value.toString() + '\n');
		}

		return  "Production:\n" +
				"ID: " + id + '\n' +
				"TV-code: " + tvCode + '\n' +
				"Name: '" + name + '\'' + '\n' +
				"Release Date: " + releaseDate + '\n' +
				"State: " + state + '\n' +
				"Associated Producer: '" + associatedProducerEmail + '\'' + '\n' +
				"Cast: " + "\n" + castString;
	}
}
