package gruppe15.domain;

import Designklassediagram.ProducerInterface;

public class Producer extends User implements ProducerInterface {

	private Production production;

	private CastCatalog castCatalog;

	private ProductionCatalog productionCatalog;

	public Producer(String name, String username, String email) {

	}

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
