package persistence;

import domain.Cast;
import domain.IPersistenceCast;
import domain.Production;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CastMembersHandler implements IPersistenceCast {

    File castMembersFile = new File("data\\castMembers.csv");
    File rolesFile = new File("data\\roles.csv");

    @Override
    public List<Cast> getCastMembers(String searchString) {
        return null;
    }

    /**
     * Return a list of cast members from the given ids.
     * @param ids the ids of the cast members to find.
     * @return a list of cast members with the given id.
     */
    private List<Cast> getCastMembers(int[] ids) {
        List<Cast> castMembers = new ArrayList<>();

        try {
            Scanner castScanner = new Scanner(castMembersFile);
            castScanner.useDelimiter(";");

            // Iterates through every line in the cast member file
            while (castScanner.hasNextLine()) {
                int curCastMemberId = castScanner.nextInt();    // The id of the cast member on the current line

                // Checks the id to find out if its one of the given ids in the method signature.
                for (int idNum = 0; idNum < ids.length; idNum++) {
                    // Checks if the current cast members id is equal to one of the ids given in the method signature
                    if (curCastMemberId == ids[idNum]) {
                        Cast newCastMember = new Cast(  // Creates the cast member:
                                curCastMemberId,        // id
                                castScanner.next(),     // first name
                                castScanner.next(),     // last name
                                castScanner.next());    // email

                        // Finds the cast member's roles
                        String[] roleIdsAsStrings = castScanner.next().split(",");   // Gets the role ids
                        int[] roleIds = new int[roleIdsAsStrings.length];      // Creates an int array where the
                        // converted values are stored.

                        // Converts every string id value to an integer value.
                        for (int i = 0; i < roleIdsAsStrings.length; i++) {
                            roleIds[i] = Integer.parseInt(roleIdsAsStrings[i]);   // Converts to the id string to an int
                        }

                        // Adds all the roles to this cast member from the given ids.
                        addRolesToCastMember(roleIds, newCastMember);

                        // Adds the current cast member to the result list
                        castMembers.add(newCastMember);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Returns the result list
        return castMembers;
    }

    /**
     * Adds all the relevant roles to the given cast member from the ids given.
     * @param ids the ids of the roles. These ids are found only in the persistence layer
     * @param castMember the cast member to which the roles are assigned.
     */
    private void addRolesToCastMember(int[] ids, Cast castMember) {
        // TODO: Should we limit to only add the roles from the current production?
        //  Now we assign every role even though it has no relevance to the current production.

        try {
            Scanner rolesScanner = new Scanner(rolesFile);
            rolesScanner.useDelimiter(";");

            // Iterates through every line in the roles file.
            while (rolesScanner.hasNextLine()) {
                int currentRoleId = rolesScanner.nextInt();    // Gets the role id on the current line

                // Checks the id to find out if its one of the given ids in the method signature.
                for (int i = 0; i < ids.length; i++) {

                    // Is the current role id the same as one of the given ids to look for.
                    if (currentRoleId == ids[i]) {
                        // We get all the elements from the current line.
                        String[] roleElements = rolesScanner.nextLine().split(";");
                        int productionId = Integer.parseInt(roleElements[2]);   // Gets the production id.

                        // Declares the production instance to be assigned to this role
                        Production currentProduction = null;

                        /*
                        // Checks through the loaded production to see if the production already exists as an instance
                        for (int j = 0; j < loadedProductions.size(); j++) {
                            if (loadedProductions.get(j).getId() == productionId) {
                                currentProduction = loadedProductions.get(j);
                                continue;
                            }
                        }
                         */

                        /*
                        // If no production was found in the loaded productions, then get it from the ProductionHandler
                        if (currentProduction == null) {
                            // Finds the production from the current id from the ids array
                            // The list should only contain one production. Hence the index of 0.
                            currentProduction = getProductions(String.valueOf(ids[i])).get(0);
                        }
                         */

                        // Adds the role to the cast member
                        castMember.addRole(roleElements[1], currentProduction);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCastMembers(List<Cast> castMembers) {

    }
}
