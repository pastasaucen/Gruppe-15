package domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Date;

public class ProductionCatalog {

	private static ProductionCatalog instance;
	private static IPersistenceProduction persistenceProduction;

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

	/**
	 * Saves temporarily found productions and sets text in the search field.
	 * @param //Searches for the input. Can either be id or name
	 * @return
	 */
	public boolean getProduction(String nameOrId) {
		// TODO Will be changed when the presentation layer is created.


		 productions = persistenceProduction.getProductions(nameOrId);


		if(productions == null) {
			// Skriv at produktionen ikke eksistere, når gui virker
			return false;
		}

		else {
			//Print alle produktioner
			for(Production p : productions) {
				System.out.println(p.toString());
			}
			return true;
		}
	}

}
