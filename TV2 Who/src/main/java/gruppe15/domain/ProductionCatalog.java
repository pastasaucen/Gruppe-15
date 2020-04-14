package gruppe15.domain;

import java.util.List;

public class ProductionCatalog {

	private static ProductionCatalog instance;

	private Production[] production;

	private ProductionCatalog() {

	}

	public static ProductionCatalog getInstance() {
		if (instance == null) {
			instance = new ProductionCatalog();
		}

		return instance;
	}

	public void addProduction(Production newProduction) {

	}

	public List<Production> searchForProduction(String nameOrId) {
		return null;
	}

}
