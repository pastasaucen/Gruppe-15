package gruppe15.domain;


import java.util.Date;

public class Producer extends User implements ProducerInterface {

	private Production production;

	private CastCatalog castCatalog;

	private ProductionCatalog productionCatalog;

	public Producer(String name, String username, String email) {

	}

	@Override
	public void createProduction(String name, Date date) {

	}

	public void addCastMember(String name) {

	}

	public void addRole(String roleName, Cast castMember) {

	}

	public void submitProduction() {

	}

	public void createCastMember(String firstName, String lastName, boolean force) {

	}

}
