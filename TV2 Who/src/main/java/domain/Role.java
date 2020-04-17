package domain;

public class Role {

	private String roleName;

	private Production production;

	public Role(String roleName, Production production) {
		this.roleName = roleName;
		this.production = production;
	}

	public Production getProduction() {
		return production;
	}

	public String getRoleName() {
		return roleName;
	}

	@Override
	public String toString() {
		return "Role{" +
				"roleName='" + roleName + '\'' +
				", production=" + production.getName() +
				'}';
	}
}
