package persistence;

import domain.*;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.Scanner;

public class ProductionsHandler implements IPersistenceProduction {

    File productionsFile = new File("data\\productions.csv");
    File castMembersFile = new File("data\\castMembers.csv");
    File rolesFile = new File("data\\roles.csv");

    // This list contains all of the productions already loaded. This stops an endless loop from happening.
    List<Production> loadedProductions = new ArrayList<>();

    @Override
    public List<Production> getProductions(String searchString) {
        List<Production> productions = new ArrayList<Production>();

        try {
            Scanner productionScanner = new Scanner(productionsFile);

            // Iterates through the lines in the production file
            while (productionScanner.hasNextLine()) {
                // Splits the lines into separate elements
                String[] elements = productionScanner.nextLine().split(";");  // The files are semicolon separated

                // First the id is checked, next the production is checked
                // We use contains so that we accommodate for half sentences or names.
                if (elements[0].equals(searchString) || elements[1].contains(searchString)) {
                    int productionId = Integer.parseInt(elements[0]);         // Parses the id as an integer
                    Date releaseDate = Date.valueOf(elements[2]);   // Parses the date as an SQL Date.

                    // We instantiate an instance of the production found
                    Production newProduction = new Production(
                            productionId,                   // id
                            elements[1],                    // name
                            releaseDate,                    // release date
                            State.valueOf(elements[3]),     // state
                            elements[4]);                   // associated producer email

                    // Finds the cast members in this production
                    String[] idAsStrings = elements[5].split(",");    // Gets the ids as strings
                    int[] castMemberIds = new int[idAsStrings.length];      // Makes it as long as the string array

                    for (int i = 0; i < idAsStrings.length; i++) {
                        castMemberIds[i] = Integer.parseInt(idAsStrings[i]);    // Converts to the id string to an int
                    }

                    // Adds the loaded production to the loaded production list
                    loadedProductions.add(newProduction);

                    // Retrieves the relevant cast members //TODO Make use of the IPersistenceCast interface...
                    List<Cast> castMembers = getCastMembers(castMemberIds, productionId);
                    // Adds all the found cast members to the production
                    for (int i = 0; i < castMembers.size(); i++) {
                        newProduction.addCastMember(castMembers.get(i));
                    }

                    // Adds this production to the result list
                    productions.add(newProduction);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Returns the result list
        return productions;
    }

    /**
     * Return a list of cast members from the given ids.
     * @param ids the ids of the cast members to find.
     * @return a list of cast members with the given id.
     */
    private List<Cast> getCastMembers(int[] ids, int productionId) {
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
                        addRolesToCastMember(productionId ,roleIds, newCastMember);

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
     * Adds the relevant role to the given cast member from the id given.
     * @param productionId the id of the production
     * @param roleIds the id of the roles the given castMember has
     * @param castMember the cast member to which the role is assigned.
     */
    private void addRolesToCastMember(int productionId, int[] roleIds, Cast castMember) {
        // TODO: Should we limit to only add the roles from the current production?
        //  Now we assign every role even though it has no relevance to the current production.

        try {
            Scanner rolesScanner = new Scanner(rolesFile);
            rolesScanner.useDelimiter(";");

            // Iterates through every line in the roles file.
            while (rolesScanner.hasNextLine()) {

                // We get all the elements from the current line.
                String[] roleElements = rolesScanner.nextLine().split(";");
                int roleId = Integer.parseInt(roleElements[0]);
                int curProductionId = Integer.parseInt(roleElements[2]);   // Gets the production id.

                // Is the current role id the same as one of the given production id to look for.
                if (curProductionId == productionId) {

                    // For every id in roleIds
                    for (int id : roleIds) {
                        if (id == roleId) {
                            // Declares the production instance to be assigned to this role
                            Production currentProduction = null;

                            // Checks through the loaded production to see if the production already exists as an instance
                            for (int j = 0; j < loadedProductions.size(); j++) {
                                if (loadedProductions.get(j).getId() == productionId) {
                                    currentProduction = loadedProductions.get(j);
                                    continue;
                                }
                            }

                            // Adds the role to the cast member
                            castMember.addRole(roleElements[1], currentProduction);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveProduction(Production production) {

    }
}
