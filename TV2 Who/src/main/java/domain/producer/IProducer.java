package domain.producer;

import domain.Cast;
import domain.Production;

import java.sql.Date;
import java.util.List;

public interface IProducer {

	void createProduction(String name, Date date);

	List<Production> getAssociatedProductions();

	void addCastMember(String name);

	void addCastMember(Cast cast, Production production);

	void addRole(String roleName, Cast castMember);

	void addRole(String roleName, Cast castMember, Production production);

	void submitProduction();

	void createCastMember(String firstName, String lastName, String email, String bio);

	public Production getProduction();

}
