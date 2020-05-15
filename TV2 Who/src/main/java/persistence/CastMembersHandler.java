package persistence;

import domain.Cast;
import domain.Production;
import domain.persistenceInterfaces.IPersistenceCast;

import java.util.List;

public class CastMembersHandler implements IPersistenceCast {

    @Override
    public List<Cast> getCastMembers(String searchString) {
        return null;
    }

    @Override
    public List<Cast> getAllCastMembers(Production production) {

        return null;
    }

    @Override
    public void saveCastMembers(List<Cast> castMembers) {

    }

    @Override
    public void addRole(String roleName, Cast castMember, Production production) {

    }
}
