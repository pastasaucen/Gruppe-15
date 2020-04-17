package persistence;

import domain.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.*;
import java.io.File;

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
                    String[] castMemberIdsAsString = elements[5].split(",");    // Gets the ids as strings
                    int[] castMemberIds = new int[castMemberIdsAsString.length];      // Makes it as long as the string array

                    for (int i = 0; i < castMemberIdsAsString.length; i++) {
                        castMemberIds[i] = Integer.parseInt(castMemberIdsAsString[i]);    // Converts to the id string to an int
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

            productionScanner.close();

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

            // Iterates through every line in the cast member file
            while (castScanner.hasNextLine()) {
                String line = castScanner.nextLine();
                String[] elements = line.split(";");
                int curCastMemberId = Integer.parseInt(elements[0]);    // The id of the cast member on the current line

                // Checks the id to find out if its one of the given ids in the method signature.
                for (int idNum = 0; idNum < ids.length; idNum++) {
                    // Checks if the current cast members id is equal to one of the ids given in the method signature
                    if (curCastMemberId == ids[idNum]) {
                        Cast newCastMember = new Cast(  // Creates the cast member:
                                curCastMemberId,        // id
                                elements[1],     // first name
                                elements[2],     // last name
                                elements[3]);    // email

                        // Finds the cast member's roles
                        String[] roleIdsAsStrings = elements[4].split(",");   // Gets the role ids
                        int[] roleIds = new int[roleIdsAsStrings.length];      // Creates an int array where the
                                                                               // converted values are stored.

                        // Converts every string id value to an integer value.
                        for (int i = 0; i < roleIdsAsStrings.length; i++) {
                            roleIds[i] = Integer.parseInt(roleIdsAsStrings[i].replaceAll("[^\\p{Print}]", ""));   // Converts to the id string to an int
                        }

                        // Adds all the roles to this cast member from the given ids.
                        addRolesToCastMember(productionId ,roleIds, newCastMember);

                        // Adds the current cast member to the result list
                        castMembers.add(newCastMember);
                    }
                }
            }

            castScanner.close();

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

            rolesScanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveProduction(Production production) { // This saves a new production
        List<Integer> productionIds = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(productionsFile);
            while (scanner.hasNextLine()){
                String[] elements = scanner.nextLine().split(";");
                productionIds.add(Integer.parseInt(elements[0]));
            }
            scanner.close();

            int highestId = 0;
            if (productionIds.size() > 0) {
                highestId = Collections.max(productionIds);
            }

            int newId = highestId + 1;
            production.setId(newId);

            String castMemberIds = "";
            for (int i = 0; i < production.getCast().size(); i++) {
                if (i == 0) {
                    castMemberIds += String.valueOf(saveCastMember(production.getCast().get(i), newId));
                } else {
                    castMemberIds += "," + saveCastMember(production.getCast().get(i), newId);
                }
            }

            PrintWriter printWriter = new PrintWriter(new FileOutputStream(productionsFile, true));
            printWriter.println(newId + ";" +
                    production.getName() + ";" +
                    production.getReleaseDate() + ";" +
                    production.getState() + ";" +
                    production.getAssociatedProducerEmail() + ";" + castMemberIds
                    );
            printWriter.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private int saveCastMember(Cast castMember, int productionId){

        /* Saving the roles for the cast member */
        List<Integer> newRoleIds = new ArrayList<>();       // All the new role ids written to the document

        try {
            List<Integer> roleIds = new ArrayList<>();      // All the role ids
            int highestRoleId = 0;                          // The highest role id

            Scanner roleScanner = new Scanner(rolesFile);

            // Goes through every line. The should not exist in the list because the production is entirely new.
            while (roleScanner.hasNextLine()){
                String[] elements = roleScanner.nextLine().split(";");
                roleIds.add(Integer.parseInt(elements[0]));     // The current lines role id is saved
            }
            roleScanner.close();

            if (roleIds.size() > 0) {
                highestRoleId = Collections.max(roleIds);   // Determines the highest id on the list
            }

            PrintWriter rolePrintWriter = new PrintWriter(new FileOutputStream(rolesFile, true));
            // For every role the cast member has. The person can have several roles in one production.
            for (int i = 0; i < castMember.getRoles().size(); i++) {
                Role role = castMember.getRoles().get(i);   // The current role

                // If the current role is from the same production as the production
                if (role.getProduction().getId() == productionId){
                    // Add the role to the list
                    rolePrintWriter.println(++highestRoleId + ";" + role.getRoleName() + ";" + productionId);
                    newRoleIds.add(highestRoleId);    // Saves the new role id to add to the cast member
                }
            }
            rolePrintWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /* Saving the cast member */
        List<Integer> castMemberIds = new ArrayList<>();    // All the cast members ids

        String castMembersFileText = "";        // The text to overwrite the text file with
        int castMemberID = -1;                  // The new cast members assigned id

        try {
            Scanner castMemberScanner = new Scanner(castMembersFile);

            boolean foundCastMember = false;    // Determines whether the cast member in the production already exists
                                                //  from a separate production.

            // Iterates through every line
            while (castMemberScanner.hasNextLine()) {
                String lineText = castMemberScanner.nextLine();       // The text on the current line
                String[] elements = lineText.split(";");        // The line split into separate strings
                castMemberID = Integer.parseInt(elements[0]);   // The current lines cast member id
                castMemberIds.add(castMemberID);                // The id is added to the list of ids

                // If the current lines cast id is the same as the cast member to save, then it exists in the file
                if (castMemberID == castMember.getId()) {
                    foundCastMember = true;         // Remembers that we found the cast member

                    // The new role ids to add to the existing cast member in the file.
                    String newRoleIdString = "";
                    for (int i = 0; i < newRoleIds.size(); i++) {
                        newRoleIdString += ("," + newRoleIds.get(i));
                    }

                    // Saves a string that needs to replace the cast member line.
                    String newLine = castMemberID + ";" +
                            castMember.getFirstName() + ";" +
                            castMember.getLastName() + ";" +
                            castMember.getEmail() + ";" +
                            elements[4] + newRoleIdString + "\n";
                    castMembersFileText += newLine;

                } else {
                    // If the current line's cast member isn't the one to save, then just saves it as it is.
                    castMembersFileText += lineText + "\n";
                }
            }

            castMemberScanner.close();

            // If we didn't find the cast member in the already saved cast members, then add it
            if (!foundCastMember) {
                // A string containing the role ids of the cast member to add
                String newRoleIdString = "";
                for (int i = 0; i < newRoleIds.size(); i++) {
                    if (i == 0) {
                        newRoleIdString += (String.valueOf(newRoleIds.get(i)));
                    } else {
                        newRoleIdString += ("," + newRoleIds.get(i));
                    }
                }

                // Adds the new cast member to the line
                int newId = 1;
                if (castMemberIds.size() > 0) {
                    newId = Collections.max(castMemberIds)+1;
                }
                castMemberID = newId;

                castMembersFileText += (newId + ";" +
                        castMember.getFirstName() + ";" +
                        castMember.getLastName() + ";" +
                        castMember.getEmail() + ";" + newRoleIdString + "\n");
            }

            PrintWriter printToCastMembersFile = new PrintWriter(castMembersFile);
            printToCastMembersFile.print(castMembersFileText);
            printToCastMembersFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return castMemberID;
    }
}
