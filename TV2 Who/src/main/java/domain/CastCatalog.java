package domain;

import java.util.ArrayList;
import java.util.List;

public class CastCatalog {

    private static CastCatalog instance;
    // Temporary until the persistence layer is implemented. We always want the newest data from the persistence layer.
    private List<Cast> cast = new ArrayList<>();

    private CastCatalog() {
        // Some cast members to try on

        cast.add(new Cast(-1, "Bob", "Bobsen", "bobsen@mail.com"));
        cast.add(new Cast(-1, "Karen", "Bobsen", "bobsen@mail.com"));
        cast.add(new Cast(-1, "Bob", "Sørensen", "bobsen@mail.com"));

    }

    int numberOfCastMembers = 0; // makes sure that no one has the same id.


    public static CastCatalog getInstance() {
        if (instance == null) {
            instance = new CastCatalog();
        }
        return instance;
    }

    /**
     * Finds the cast member with the given first name and last name.
     *
     * @param firstName
     * @param lastName
     * @return a list of cast members with the given names
     */
    public List<Cast> searchForCast(String firstName, String lastName) {
        // TODO When the persistence layer is implemented, use this to search for the correct cast member.

        List<Cast> relevantCastMembers = new ArrayList<>();

        for (Cast currentCast : cast) {
            // Do the current cast member have the same first and last name as the search words?
            if (currentCast.getFirstName().indexOf(firstName)!=-1) {
                relevantCastMembers.add(currentCast); // Adds this member to the return list
            }

        }
        return relevantCastMembers;
    }


    /**
     * creates a cast member and adds it to the database (for now arraylist)
     *
     * @param firstName
     * @param lastName
     * @param email
     */
    public void createCastMember(String firstName, String lastName, String email) {
        numberOfCastMembers++;
        cast.add(new Cast(numberOfCastMembers, firstName, lastName, email));
    }

}
