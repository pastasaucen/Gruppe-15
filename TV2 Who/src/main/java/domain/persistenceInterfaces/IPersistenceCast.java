package domain.persistenceInterfaces;

import domain.Cast;
import domain.Production;
import domain.User;
import domain.producer.Producer;

import java.util.List;

public interface IPersistenceCast {

    /**
     * Return a list of cast instances from the persistence layer. The searchString can either be a first name, last
     * name or an email.
     * @param searchString a first name and/or last name, or email.
     * @return a list of relevant cast member instances.
     */
    List<Cast> getCastMembers(String searchString, User currentUser);

    /**
     * Saves the given cast members to the persistence layer.
     * @param castMembers the cast member to be saved.
     */
    void saveCastMembers(List<Cast> castMembers);
}
