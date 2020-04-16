package domain;

import java.util.List;

	ProductionCatalog productionCatalog;
public class TV2Who implements ITv2Who {

	private static TV2Who instance = null;

	private TV2Who(){

	}

	/**
	 * Used to retrieve the singleton instance of the TV2 Who class
	 * @return
	 */
	public static TV2Who getInstance(){
		if(instance == null){
			instance = new TV2Who();
		}
		return instance;
	}

	public void findProduction(String nameOrId) {

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
	/**
	 * Prepares a list of cast members for use in the presentation layer
	 * @param firstName
	 * @param lastName
	 * @return List(Cast)
	 */
	@Override
	public List<Cast> prepareCastSearchList(String firstName, String lastName){
		return CastCatalog.getInstance().searchForCast(firstName,lastName);
	}

	/**
	 * Prepares a list of cast members for use in the presentation layer
	 * @param nameOrId
	 * @return
	 */
	@Override
	public List<Production> prepareProductionSearchList(String nameOrId){
		return ProductionCatalog.getInstance().searchForProduction(nameOrId);
	}

}
