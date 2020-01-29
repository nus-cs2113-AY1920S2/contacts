import java.util.Scanner;


public class Contacts0 {

    public static void main(String[] args) {
        final Scanner SCANNER = new Scanner(System.in);
        System.out.println("|| ===================================================");
        System.out.println("|| ===================================================");
        System.out.println("|| Contacts - Version 0.0");
        System.out.println("|| Welcome to Contacts!");
        System.out.println("|| ===================================================");
        String[][] list = new String[100][3];
        int count = 0;
        while (true) {
            System.out.print("|| " + "Enter command: ");
            String inputLine = SCANNER.nextLine();
            while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == '#') {
                inputLine = SCANNER.nextLine();
            }
            String userCommand = inputLine;
            System.out.println("|| [Command entered:" + userCommand + "]");
            String feedback;
            final String[] split = userCommand.trim().split("\\s+", 2);
            final String[] commandTypeAndParams = split.length == 2 ? split : new String[]{split[0], ""};
            final String commandType = commandTypeAndParams[0];
            final String commandArgs = commandTypeAndParams[1];
            switch (commandType) {
            case "add":
                String[] decodeResult = null;
                final String matchAnyPersonDataPrefix = "p/" + '|' + "e/";
                final String[] splitArgs = commandArgs.trim().split(matchAnyPersonDataPrefix);
                if (splitArgs.length == 3 // 3 arguments
                        && !splitArgs[0].isEmpty() // non-empty arguments
                        && !splitArgs[1].isEmpty()
                        && !splitArgs[2].isEmpty()) {
                    final String[] person1 = new String[3];
                    final int indexOfPhonePrefix = commandArgs.indexOf("p/");
                    final int indexOfEmailPrefix = commandArgs.indexOf("e/");// name is leading substring up to first data prefix symbol
                    int indexOfFirstPrefix = Math.min(indexOfEmailPrefix, indexOfPhonePrefix);
                    person1[0] = commandArgs.substring(0, indexOfFirstPrefix).trim();
                    String result;
                    final int indexOfPhonePrefix1 = commandArgs.indexOf("p/");
                    final int indexOfEmailPrefix1 = commandArgs.indexOf("e/");// phone is last arg, target is from prefix to end of string
                    if (indexOfPhonePrefix1 > indexOfEmailPrefix1) {
                        result = commandArgs.substring(indexOfPhonePrefix1, commandArgs.length()).trim().replace("p/", "");
                    } else {
                        result = commandArgs.substring(indexOfPhonePrefix1, indexOfEmailPrefix1).trim().replace("p/", "");
                    }
                    person1[1] = result;
                    String result1;
                    final int indexOfPhonePrefix2 = commandArgs.indexOf("p/");
                    final int indexOfEmailPrefix2 = commandArgs.indexOf("e/");// email is last arg, target is from prefix to end of string
                    if (indexOfEmailPrefix2 > indexOfPhonePrefix2) {
                        result1 = commandArgs.substring(indexOfEmailPrefix2, commandArgs.length()).trim().replace("e/", "");
                    } else {
                        result1 = commandArgs.substring(indexOfEmailPrefix2, indexOfPhonePrefix2).trim().replace("e/", "");
                    }
                    person1[2] = result1;
                    decodeResult = !person1[0].isEmpty()
                            && !person1[1].isEmpty()
                            && !person1[2].isEmpty() && person1[2].contains("@") ? person1 : null;
                }
                if (decodeResult == null) {
                    feedback = String.format("Invalid command format: %1$s " + (System.lineSeparator() + "|| ") 
                            + "%2$s", "add", String.format("%1$s: %2$s", "add", "Adds a person to contacts.") + (System.lineSeparator() + "|| ")
                            + String.format("\tParameters: %1$s", "NAME "
                            + "p/" + "PHONE_NUMBER "
                            + "e/" + "EMAIL") + (System.lineSeparator() + "|| ")
                            + String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + (System.lineSeparator() + "|| "));
                    break;
                }
                list[count] = decodeResult;
                count++;
                feedback = String.format("New person added: %1$s, Phone: %2$s, Email: %3$s",
                        decodeResult[0], decodeResult[1], decodeResult[2]);
                break;
            case "list":
                String[][] toBeDisplayed = list;
                final StringBuilder messageAccumulator = new StringBuilder();
                for (int i = 0; i < count; i++) {
                    final String[] person = toBeDisplayed[i];
                    final int displayIndex = i + 1;
                    messageAccumulator.append('\t').append(String.format("%1$d. ", displayIndex))
                            .append(String.format("%1$s  Phone Number: %2$s  Email: %3$s", person[0], person[1], person[2]))
                            .append(System.lineSeparator()).append("|| ");
                }
                String listAsString = messageAccumulator.toString();
                for (String m1 : new String[]{listAsString}) {
                    System.out.println("|| " + m1);
                }
                feedback = String.format("%1$d persons found!", count);
                break;
            case "clear":
                list = new String[100][3];
                count = 0;
                feedback = "Contacts have been cleared!";
                break;
            case "help":
                feedback = (String.format("%1$s: %2$s", "add", "Adds a person to contacts.") + (System.lineSeparator() + "|| ")
                        + String.format("\tParameters: %1$s", "NAME "
                        + "p/" + "PHONE_NUMBER "
                        + "e/" + "EMAIL") + (System.lineSeparator() + "|| ")
                        + String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + (System.lineSeparator() + "|| ")) + (System.lineSeparator() + "|| ")
                        + (String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + (System.lineSeparator() + "|| ")
                        + String.format("\tExample: %1$s", "list") + (System.lineSeparator() + "|| ")) + (System.lineSeparator() + "|| ")
                        + (String.format("%1$s: %2$s", "clear", "Clears all contacts.") + (System.lineSeparator() + "|| ")
                        + String.format("\tExample: %1$s", "clear") + (System.lineSeparator() + "|| ")) + (System.lineSeparator() + "|| ")
                        + (String.format("%1$s: %2$s", "exit", "Exits the program.")+ (System.lineSeparator() + "|| ")
                        + String.format("\tExample: %1$s", "exit")) + (System.lineSeparator() + "|| ") + (System.lineSeparator() + "|| ")
                        + (String.format("%1$s: %2$s", "help", "Shows program usage instructions.")+ (System.lineSeparator() + "|| ")
                        + String.format("\tExample: %1$s", "help"));
                break;
            case "exit":
                for (String m1 : new String[]{"Exiting Contacts... Good bye!",
                        "===================================================",
                        "==================================================="}) {
                    System.out.println("|| " + m1);
                }
                System.exit(0);
                // Fallthrough
            default:
                feedback = String.format("Invalid command format: %1$s " + (System.lineSeparator() + "|| ") 
                        + "%2$s", commandType, (String.format("%1$s: %2$s", "add", "Adds a person to contacts.") + (System.lineSeparator() + "|| ")
                        + String.format("\tParameters: %1$s", "NAME "
                        + "p/" + "PHONE_NUMBER "
                        + "e/" + "EMAIL") + (System.lineSeparator() + "|| ")
                        + String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + (System.lineSeparator() + "|| ")) + (System.lineSeparator() + "|| ")
                        + (String.format("%1$s: %2$s", "find", "Finds all persons whose names contain any of the specified "
                        + "keywords (case-sensitive) and displays them as a list with index numbers.") + (System.lineSeparator() + "|| ")
                        + String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + (System.lineSeparator() + "|| ")
                        + String.format("\tExample: %1$s", "find" + " alice bob charlie") + (System.lineSeparator() + "|| ")) + (System.lineSeparator() + "|| ")
                        + (String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + (System.lineSeparator() + "|| ")
                        + String.format("\tExample: %1$s", "list") + (System.lineSeparator() + "|| ")) + (System.lineSeparator() + "|| ")
                        + (String.format("%1$s: %2$s", "clear", "Clears all contacts.") + (System.lineSeparator() + "|| ")
                        + String.format("\tExample: %1$s", "clear") + (System.lineSeparator() + "|| ")) + (System.lineSeparator() + "|| ")
                        + (String.format("%1$s: %2$s", "exit", "Exits the program.")
                        + String.format("\tExample: %1$s", "exit")) + (System.lineSeparator() + "|| ")
                        + (String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
                        + String.format("\tExample: %1$s", "help")));
                break;
            }
            for (String m : new String[]{feedback, "==================================================="}) {
                System.out.println("|| " + m);
            }
        }
    }

}
