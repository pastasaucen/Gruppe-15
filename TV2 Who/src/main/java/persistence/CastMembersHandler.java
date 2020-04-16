package persistence;

import domain.Cast;
import domain.IPersistenceCast;

import java.util.List;

public class CastMembersHandler implements IPersistenceCast {
    @Override
    public List<Cast> getCastMembers(String searchString) {
        return null;
    }

    @Override
    public void saveCastMembers(List<Cast> castMembers) {

    }
}
