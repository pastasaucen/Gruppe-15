package domain;

public class TV2Who {

	ProductionCatalog productionCatalog;

	public TV2Who() {
		this.productionCatalog = ProductionCatalog.getInstance();
	}

	/**
	 * Searches for productions through productionCatalog by name or ID.
	 * @param nameOrId
	 */
	public void getProduction(String nameOrId) {
		productionCatalog.getProduction(nameOrId);
	}
}
