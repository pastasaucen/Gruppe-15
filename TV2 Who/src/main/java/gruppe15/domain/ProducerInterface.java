package gruppe15.domain;

import java.util.Date;

public interface ProducerInterface {

	void createProduction(String name, Date date);

	void addCastMember(String name);

	void addRole(String roleName, Cast castMember);

	void submitProduction();

	void createCastMember(String firstName, String lastName, String email);

}
