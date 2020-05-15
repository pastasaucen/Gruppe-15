package domain;

public class Role {

	private String roleName;

	private Production production;

	private int id;

	public Role(int id, String roleName, Production production) {
		this.roleName = roleName;
		this.production = production;
		this.id = id;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Production getProduction() {
		return production;
	}

	public String getRoleName() {
		return roleName;
	}

	@Override
	public String toString() {
		return "Role Name: '" + roleName + '\'' +
				", Production: " + production.getName();
	}
}
