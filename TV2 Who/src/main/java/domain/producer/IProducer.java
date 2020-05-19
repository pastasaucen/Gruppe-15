package domain.producer;

import domain.Cast;
import domain.Production;

import java.sql.Date;

public interface IProducer {

	void createProduction(String name, Date date);

	void addCastMember(String name);

	void addCastMember(Cast cast, Production production);

	void addRole(String roleName, Cast castMember);

	void addRole(String roleName, Cast castMember, Production production);

	void submitProduction();

	void createCastMember(String firstName, String lastName, String email, String bio);

}
