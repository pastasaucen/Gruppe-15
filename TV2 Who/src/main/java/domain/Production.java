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

	public Production(int id, String name, Date releaseDate, State state, String associatedProducerEmail) {
		this.id = id;
		this.name = name;
		this.releaseDate = releaseDate;
		this.state = state;
		this.associatedProducerEmail = associatedProducerEmail;
		this.castList = CastCatalog.getInstance().getAllCastMembers(this);
	}

	public void addCastMember(Cast castMember) {
		castList.add(castMember);
}

	//Returns updated list, after adding a role to a cast.
	public void addRole(String roleName, Cast castMember) {
		//If castmember exists on the production.
		if (castList.contains(castMember)) {
			// castMember exists
			// TODO Ask if they want to continue here in GUI
		}
		//Try database insertion of role, catch SQL exceptions
		//Updaterer midlertidige liste til gui
		int index = castList.indexOf(castMember);
		castList.get(index).addRole(roleName, this);
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

	public List<Cast> getCastList() {
		return castList;
	}

	@Override
	public String toString() {
		String castString = "";

		for (int i = 0; i < castList.size(); i++) {
			castString += '\t' + (castList.get(i).toString() + '\n');
		}

		return "Production:\n" +
				"id=" + id + '\n' +
				"name ='" + name + '\'' + '\n' +
				"releaseDate =" + releaseDate + '\n' +
				"state =" + state + '\n' +
				"associatedProducerEmail ='" + associatedProducerEmail + '\'' + '\n' +
				"cast =" + "\n" + castString;
	}

	public void setAssociatedProducerEmail(String associatedProducerEmail) {
		this.associatedProducerEmail = associatedProducerEmail;
	}
}
