package gruppe15.domain;

import java.util.ArrayList;
import java.util.List;

public class ProductionCatalog {

	private static ProductionCatalog instance;

	private List<Production> productions = new ArrayList<>();

	private ProductionCatalog() {

	}

	public static ProductionCatalog getInstance() {
		if (instance == null) {
			instance = new ProductionCatalog();
		}

		return instance;
	}

	public void addProduction(Production newProduction) {
		// TODO Will be changed when the persistence layer is created.
		productions.add(newProduction);
		System.out.println(productions);

	}

	public List<Production> searchForProduction(String nameOrId) {
		return null;
	}

}
