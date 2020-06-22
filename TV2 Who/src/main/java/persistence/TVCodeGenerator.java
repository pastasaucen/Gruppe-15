package persistence;

public class TVCodeGenerator {

    private static char[] chars = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * Takes a TV-code and returns a new incremented code.
     * @param oldTVCode the old code.
     * @return a tv-code one digit larger than the old code.
     */
    public static String incrementTVCode(String oldTVCode) {

        // Convert characters to indexes
        int[] indexes = new int[oldTVCode.length()];

        String stringChar = new String(chars);

        for (int i = indexes.length-1; i >= 0; i--) {
            indexes[i] = stringChar.indexOf(oldTVCode.charAt(i));
        }

        // Increment the indexes
        for (int i = indexes.length-1; i >= 0; i--) {
            indexes[i]++;

            if (indexes[i] <= chars.length-1) {
                break;
            } else {
                indexes[i] = 0;
                if (i-1 == -1) {
                    System.out.println("There are no more possible TV-codes to be generated!");
                }
            }
        }

        // Convert back
        char[] newTVCode = new char[oldTVCode.length()];

        for (int i = newTVCode.length-1; i >= 0; i--) {
            newTVCode[i] = chars[indexes[i]];
        }

        return new String(newTVCode);
    }

}
