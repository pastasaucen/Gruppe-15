package gruppe15.domain;

import java.sql.Date;

public interface IProducer {

	void createProduction(String name, Date date);

	void addCastMember(String name);

	void addRole(String roleName, Cast castMember);

	void submitProduction();

	void createCastMember(String firstName, String lastName, boolean force);

}
