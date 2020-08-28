import java.util.Scanner;


/**
 * This class is used to maintain a list of person data which are saved
 * in a text file.
 **/
public class Contacts1 {


    /**
     * Version info of the program.
     */
    private static final String VERSION = "Contacts - Version 1.0";

    /**
     * A decorative prefix added to the beginning of lines printed by AddressBook
     */
    private static final String LINE_PREFIX = "|| ";

    /**
     * A platform independent line separator.
     */
    private static final String LS = System.lineSeparator() + LINE_PREFIX;

    /*
     * NOTE : ==================================================================
     * These messages shown to the user are defined in one place for convenient
     * editing and proof reading. Such messages are considered part of the UI
     * and may be subjected to review by UI experts or technical writers. Note
     * that Some of the strings below include '%1$s' etc to mark the locations
     * at which java String.format(...) method can insert values.
     * =========================================================================
     */
    private static final String MESSAGE_ADDED = "New person added: %1$s, Phone: %2$s, Email: %3$s";
    private static final String MESSAGE_ADDRESSBOOK_CLEARED = "Contacts have been cleared!";
    private static final String MESSAGE_COMMAND_HELP = "%1$s: %2$s";
    private static final String MESSAGE_COMMAND_HELP_PARAMETERS = "\tParameters: %1$s";
    private static final String MESSAGE_COMMAND_HELP_EXAMPLE = "\tExample: %1$s";
    private static final String MESSAGE_DISPLAY_PERSON_DATA = "%1$s  Phone Number: %2$s  Email: %3$s";
    private static final String MESSAGE_DISPLAY_LIST_ELEMENT_INDEX = "%1$d. ";
    private static final String MESSAGE_GOODBYE = "Exiting Contacts... Good bye!";
    private static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format: %1$s " + LS + "%2$s";
    private static final String MESSAGE_PERSONS_FOUND_OVERVIEW = "%1$d persons found!";
    private static final String MESSAGE_WELCOME = "Welcome to Contacts!";

    // These are the prefix strings to define the data type of a command parameter
    private static final String PERSON_DATA_PREFIX_PHONE = "p/";
    private static final String PERSON_DATA_PREFIX_EMAIL = "e/";

    private static final String COMMAND_ADD_WORD = "add";
    private static final String COMMAND_ADD_DESC = "Adds a person to contacts.";
    private static final String COMMAND_ADD_PARAMETERS = "NAME "
                                                      + PERSON_DATA_PREFIX_PHONE + "PHONE_NUMBER "
                                                      + PERSON_DATA_PREFIX_EMAIL + "EMAIL";
    private static final String COMMAND_ADD_EXAMPLE = COMMAND_ADD_WORD + " John Doe p/98765432 e/johnd@gmail.com";

    private static final String COMMAND_LIST_WORD = "list";
    private static final String COMMAND_LIST_DESC = "Displays all persons as a list with index numbers.";
    private static final String COMMAND_LIST_EXAMPLE = COMMAND_LIST_WORD;

    private static final String COMMAND_CLEAR_WORD = "clear";
    private static final String COMMAND_CLEAR_DESC = "Clears all contacts.";
    private static final String COMMAND_CLEAR_EXAMPLE = COMMAND_CLEAR_WORD;

    private static final String COMMAND_HELP_WORD = "help";
    private static final String COMMAND_HELP_DESC = "Shows program usage instructions.";
    private static final String COMMAND_HELP_EXAMPLE = COMMAND_HELP_WORD;

    private static final String COMMAND_EXIT_WORD = "exit";
    private static final String COMMAND_EXIT_DESC = "Exits the program.";
    private static final String COMMAND_EXIT_EXAMPLE = COMMAND_EXIT_WORD;

    private static final String DIVIDER = "===================================================";


    /* We use a String array to store details of a single person.
     * The constants given below are the indexes for the different data elements of a person
     * used by the internal String[] storage format.
     * For example, a person's name is stored as the 0th element in the array.
     */
    private static final int PERSON_DATA_INDEX_NAME = 0;
    private static final int PERSON_DATA_INDEX_PHONE = 1;
    private static final int PERSON_DATA_INDEX_EMAIL = 2;

    /**
     * The number of data elements for a single person.
     */
    private static final int PERSON_DATA_COUNT = 3;
    
    /**
     * Maximum number of persons that can be held.
     */
    private static final int CAPACITY = 100;

    /**
     * If the first non-whitespace character in a user's input line is this, that line will be ignored.
     */
    private static final char INPUT_COMMENT_MARKER = '#';

    /*
     * This variable is declared for the whole class (instead of declaring it
     * inside the readUserCommand() method to facilitate automated testing using
     * the I/O redirection technique. If not, only the first line of the input
     * text file will be processed.
     */
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * List of all persons.
     */
    private static String[][] allPersons;

    /**
     * Total number of persons in the list
     */
    private static int count;


    /*
     * NOTE : =============================================================
     * Notice how this method solves the whole problem at a very high level.
     * We can understand the high-level logic of the program by reading this
     * method alone.
     * If the reader wants a deeper understanding of the solution, she can go
     * to the next level of abstraction by reading the methods that are
     * referenced by the high-level method below.
     * ====================================================================
     */

    /**
     * Main entry point of the application.
     * Initializes the application and starts the interaction with the user.
     */
    public static void main(String[] args) {
        initAddressBook();
        showWelcomeMessage();
        while (true) {
            String userCommand = getUserInput();
            echoUserCommand(userCommand);
            String feedback = executeCommand(userCommand);
            showResultToUser(feedback);
        }
    }

    /*
     * NOTE : =============================================================
     * The method header comment can be omitted if the method is trivial
     * and the header comment is going to be almost identical to the method
     * signature anyway.
     * ====================================================================
     */

    private static void showWelcomeMessage() {
        showToUser(DIVIDER, DIVIDER, VERSION, MESSAGE_WELCOME, DIVIDER);
    }

    private static void showResultToUser(String result) {
        showToUser(result, DIVIDER);
    }

    /*
     * NOTE : =============================================================
     * Parameter description can be omitted from the method header comment
     * if the parameter name is self-explanatory.
     * In the method below, '@param userInput' comment has been omitted.
     * ====================================================================
     */

    /**
     * Echoes the user input back to the user.
     */
    private static void echoUserCommand(String userCommand) {
        showToUser("[Command entered:" + userCommand + "]");
    }

    /**
     * Displays the goodbye message and exits the runtime.
     */
    private static void exitProgram() {
        showToUser(MESSAGE_GOODBYE, DIVIDER, DIVIDER);
        System.exit(0);
    }


    /*
     * ===========================================
     *           COMMAND LOGIC
     * ===========================================
     */

    /**
     * Executes the command as specified by the {@code userInputString}
     *
     * @param userInputString  raw input from user
     * @return  feedback about how the command was executed
     */
    private static String executeCommand(String userInputString) {
        final String[] commandTypeAndParams = splitCommandWordAndArgs(userInputString);
        final String commandType = commandTypeAndParams[0];
        final String commandArgs = commandTypeAndParams[1];
        switch (commandType) {
        case COMMAND_ADD_WORD:
            return executeAddPerson(commandArgs);
        case COMMAND_LIST_WORD:
            return executeListAllPersonsInAddressBook();
        case COMMAND_CLEAR_WORD:
            return executeClearAddressBook();
        case COMMAND_HELP_WORD:
            return getUsageInfoForAllCommands();
        case COMMAND_EXIT_WORD:
            executeExitProgramRequest();
            // Fallthrough
        default:
            return getMessageForInvalidCommandInput(commandType, getUsageInfoForAllCommands());
        }
    }

    /**
     * Splits raw user input into command word and command arguments string
     *
     * @return  size 2 array; first element is the command type and second element is the arguments string
     */
    private static String[] splitCommandWordAndArgs(String rawUserInput) {
        final String[] split = rawUserInput.trim().split("\\s+", 2);
        return split.length == 2 ? split : new String[] { split[0] , "" }; // else case: no parameters
    }

    /**
     * Constructs a generic feedback message for an invalid command from user, with instructions for correct usage.
     *
     * @param correctUsageInfo message showing the correct usage
     * @return invalid command args feedback message
     */
    private static String getMessageForInvalidCommandInput(String userCommand, String correctUsageInfo) {
        return String.format(MESSAGE_INVALID_COMMAND_FORMAT, userCommand, correctUsageInfo);
    }

    /**
     * Adds a person (specified by the command args) to the address book.
     * The entire command arguments string is treated as a string representation of the person to add.
     *
     * @param commandArgs full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String executeAddPerson(String commandArgs) {
        // try decoding a person from the raw args
        final String[] decodeResult = decodePersonFromString(commandArgs);

        // checks if args are valid (decode result will not be present if the person is invalid)
        if (decodeResult == null) {
            return getMessageForInvalidCommandInput(COMMAND_ADD_WORD, getUsageInfoForAddCommand());
        }

        // add the person as specified
        addPersonToAddressBook(decodeResult);
        return getMessageForSuccessfulAddPerson(decodeResult);
    }

    /**
     * Constructs a feedback message for a successful add person command execution.
     *
     * @param addedPerson person who was successfully added
     * @return successful add person feedback message
     * @see #executeAddPerson(String)
     */
    private static String getMessageForSuccessfulAddPerson(String[] addedPerson) {
        return String.format(MESSAGE_ADDED,
                getNameFromPerson(addedPerson), getPhoneFromPerson(addedPerson), getEmailFromPerson(addedPerson));
    }

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of persons.
     *
     * @param personsDisplayed used to generate summary
     * @return summary message for persons displayed
     */
    private static String getMessageForPersonsDisplayedSummary(String[][] personsDisplayed) {
        return String.format(MESSAGE_PERSONS_FOUND_OVERVIEW, count);
    }


    /**
     * Clears all persons in the address book.
     *
     * @return feedback display message for the operation result
     */
    private static String executeClearAddressBook() {
        initAddressBook();
        return MESSAGE_ADDRESSBOOK_CLEARED;
    }

    /**
     * Displays all persons in the address book to the user; in added order.
     *
     * @return feedback display message for the operation result
     */
    private static String executeListAllPersonsInAddressBook() {
        String[][] toBeDisplayed = getAllPersonsInAddressBook();
        showToUser(toBeDisplayed);
        return getMessageForPersonsDisplayedSummary(toBeDisplayed);
    }

    /**
     * Requests to terminate the program.
     */
    private static void executeExitProgramRequest() {
        exitProgram();
    }

    /*
     * ===========================================
     *               UI LOGIC
     * ===========================================
     */

    /**
     * Prompts for the command and reads the text entered by the user.
     * Ignores lines with first non-whitespace char equal to {@link #INPUT_COMMENT_MARKER} (considered comments)
     *
     * @return full line entered by the user
     */
    private static String getUserInput() {
        System.out.print(LINE_PREFIX + "Enter command: ");
        String inputLine = SCANNER.nextLine();
        // silently consume all blank and comment lines
        while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == INPUT_COMMENT_MARKER) {
            inputLine = SCANNER.nextLine();
        }
        return inputLine;
    }

    /*
     * NOTE : =============================================================
     * Note how the method below uses Java 'Varargs' feature so that the
     * method can accept a varying number of message parameters.
     * ====================================================================
     */

    /**
     * Shows a message to the user
     */
    private static void showToUser(String... message) {
        for (String m : message) {
            System.out.println(LINE_PREFIX + m);
        }
    }

    /**
     * Shows the list of persons to the user.
     * The list will be indexed, starting from 1.
     *
     */
    private static void showToUser(String[][] persons) {
        String listAsString = getDisplayString(persons);
        showToUser(listAsString);
    }

    /**
     * Returns the display string representation of the list of persons.
     */
    private static String getDisplayString(String[][] persons) {
        final StringBuilder messageAccumulator = new StringBuilder();
        for (int i = 0; i < count; i++) {
            final String[] person = persons[i];
            final int displayIndex = i + 1;
            messageAccumulator.append('\t')
                              .append(getIndexedPersonListElementMessage(displayIndex, person))
                              .append(LS);
        }
        return messageAccumulator.toString();
    }

    /**
     * Constructs a prettified listing element message to represent a person and their data.
     *
     * @param visibleIndex visible index for this listing
     * @param person to show
     * @return formatted listing message with index
     */
    private static String getIndexedPersonListElementMessage(int visibleIndex, String[] person) {
        return String.format(MESSAGE_DISPLAY_LIST_ELEMENT_INDEX, visibleIndex)
                + getMessageForFormattedPersonData(person);
    }

    /**
     * Constructs a prettified string to show the user a person's data.
     *
     * @param person to show
     * @return formatted message showing internal state
     */
    private static String getMessageForFormattedPersonData(String[] person) {
        return String.format(MESSAGE_DISPLAY_PERSON_DATA,
                getNameFromPerson(person), getPhoneFromPerson(person), getEmailFromPerson(person));
    }

    /**
     * Adds a person to the address book. Saves changes to storage file.
     *
     * @param person to add
     */
    private static void addPersonToAddressBook(String[] person) {
        allPersons[count] = person;
        count++;
    }

    /**
     * Returns all persons in the address book
     */
    private static String[][] getAllPersonsInAddressBook() {
        return allPersons;
    }

    /**
     * Clears all persons in the address book and saves changes to file.
     */
    private static void initAddressBook() {
        allPersons = new String[CAPACITY][PERSON_DATA_COUNT];
        count = 0;
    }


    /*
     * ===========================================
     *             PERSON METHODS
     * ===========================================
     */

    /**
     * Returns the given person's name
     *
     * @param person whose name you want
     */
    private static String getNameFromPerson(String[] person) {
        return person[PERSON_DATA_INDEX_NAME];
    }

    /**
     * Returns given person's phone number
     *
     * @param person whose phone number you want
     */
    private static String getPhoneFromPerson(String[] person) {
        return person[PERSON_DATA_INDEX_PHONE];
    }

    /**
     * Returns given person's email
     *
     * @param person whose email you want
     */
    private static String getEmailFromPerson(String[] person) {
        return person[PERSON_DATA_INDEX_EMAIL];
    }

    /**
     * Creates a person from the given data.
     *
     * @param name of person
     * @param phone without data prefix
     * @param email without data prefix
     * @return constructed person
     */
    private static String[] makePersonFromData(String name, String phone, String email) {
        final String[] person = new String[PERSON_DATA_COUNT];
        person[PERSON_DATA_INDEX_NAME] = name;
        person[PERSON_DATA_INDEX_PHONE] = phone;
        person[PERSON_DATA_INDEX_EMAIL] = email;
        return person;
    }

    /**
     * Decodes a person from it's supposed string representation.
     *
     * @param encoded string to be decoded
     * @return if cannot decode: empty Optional
     *         else: Optional containing decoded person
     */
    private static String[] decodePersonFromString(String encoded) {
        // check that we can extract the parts of a person from the encoded string
        if (!isPersonDataExtractableFrom(encoded)) {
            return null;
        }
        final String[] decodedPerson = makePersonFromData(
                extractNameFromPersonString(encoded),
                extractPhoneFromPersonString(encoded),
                extractEmailFromPersonString(encoded)
        );
        // check that the constructed person is valid
        return isPersonDataValid(decodedPerson) ? decodedPerson : null;
    }


    /**
     * Returns true if person data (email, name, phone etc) can be extracted from the argument string.
     * Format is [name] p/[phone] e/[email], phone and email positions can be swapped.
     *
     * @param personData person string representation
     */
    private static boolean isPersonDataExtractableFrom(String personData) {
        final String matchAnyPersonDataPrefix = PERSON_DATA_PREFIX_PHONE + '|' + PERSON_DATA_PREFIX_EMAIL;
        final String[] splitArgs = personData.trim().split(matchAnyPersonDataPrefix);
        return splitArgs.length == 3 // 3 arguments
                && !splitArgs[0].isEmpty() // non-empty arguments
                && !splitArgs[1].isEmpty()
                && !splitArgs[2].isEmpty();
    }

    /**
     * Extracts substring representing person name from person string representation
     *
     * @param encoded person string representation
     * @return name argument
     */
    private static String extractNameFromPersonString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf(PERSON_DATA_PREFIX_PHONE);
        final int indexOfEmailPrefix = encoded.indexOf(PERSON_DATA_PREFIX_EMAIL);
        // name is leading substring up to first data prefix symbol
        int indexOfFirstPrefix = Math.min(indexOfEmailPrefix, indexOfPhonePrefix);
        return encoded.substring(0, indexOfFirstPrefix).trim();
    }

    /**
     * Extracts substring representing phone number from person string representation
     *
     * @param encoded person string representation
     * @return phone number argument WITHOUT prefix
     */
    private static String extractPhoneFromPersonString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf(PERSON_DATA_PREFIX_PHONE);
        final int indexOfEmailPrefix = encoded.indexOf(PERSON_DATA_PREFIX_EMAIL);

        // phone is last arg, target is from prefix to end of string
        if (indexOfPhonePrefix > indexOfEmailPrefix) {
            return removePrefixSign(encoded.substring(indexOfPhonePrefix, encoded.length()).trim(),
                    PERSON_DATA_PREFIX_PHONE);

        // phone is middle arg, target is from own prefix to next prefix
        } else {
            return removePrefixSign(
                    encoded.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim(),
                    PERSON_DATA_PREFIX_PHONE);
        }
    }

    /**
     * Extracts substring representing email from person string representation
     *
     * @param encoded person string representation
     * @return email argument WITHOUT prefix
     */
    private static String extractEmailFromPersonString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf(PERSON_DATA_PREFIX_PHONE);
        final int indexOfEmailPrefix = encoded.indexOf(PERSON_DATA_PREFIX_EMAIL);

        // email is last arg, target is from prefix to end of string
        if (indexOfEmailPrefix > indexOfPhonePrefix) {
            return removePrefixSign(encoded.substring(indexOfEmailPrefix, encoded.length()).trim(),
                    PERSON_DATA_PREFIX_EMAIL);

        // email is middle arg, target is from own prefix to next prefix
        } else {
            return removePrefixSign(
                    encoded.substring(indexOfEmailPrefix, indexOfPhonePrefix).trim(),
                    PERSON_DATA_PREFIX_EMAIL);
        }
    }

    /**
     * Returns true if the given person's data fields are valid
     *
     * @param person String array representing the person (used in internal data)
     */
    private static boolean isPersonDataValid(String[] person) {
        return isValidName(person[PERSON_DATA_INDEX_NAME])
                && isValidPhone(person[PERSON_DATA_INDEX_PHONE])
                && isValidEmail(person[PERSON_DATA_INDEX_EMAIL]);
    }

    /**
     * Returns true if the given string as a legal person name
     *
     * @param name to be validated
     */
    private static boolean isValidName(String name) {
        return !name.isEmpty();
        //TODO: implement a better validation
    }

    /**
     * Returns true if the given string as a legal person phone number
     *
     * @param phone to be validated
     */
    private static boolean isValidPhone(String phone) {
        return !phone.isEmpty();
        //TODO: implement a better validation
    }

    /**
     * Returns true if the given string is a legal person email
     *
     * @param email to be validated
     * @return whether arg is a valid person email
     */
    private static boolean isValidEmail(String email) {
        return !email.isEmpty() && email.contains("@");
        //TODO: implement a better validation
    }


    /*
     * ===============================================
     *         COMMAND HELP INFO FOR USERS
     * ===============================================
     */

    /** Returns usage info for all commands */
    private static String getUsageInfoForAllCommands() {
        return getUsageInfoForAddCommand() + LS
                + getUsageInfoForViewCommand() + LS
                + getUsageInfoForClearCommand() + LS
                + getUsageInfoForExitCommand() + LS
                + getUsageInfoForHelpCommand();
    }

    /** Returns the string for showing 'add' command usage instruction */
    private static String getUsageInfoForAddCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_ADD_WORD, COMMAND_ADD_DESC) + LS
                + String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_ADD_PARAMETERS) + LS
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_ADD_EXAMPLE) + LS;
    }

    /** Returns string for showing 'clear' command usage instruction */
    private static String getUsageInfoForClearCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_CLEAR_WORD, COMMAND_CLEAR_DESC) + LS
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_CLEAR_EXAMPLE) + LS;
    }

    /** Returns the string for showing 'view' command usage instruction */
    private static String getUsageInfoForViewCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_LIST_WORD, COMMAND_LIST_DESC) + LS
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_LIST_EXAMPLE) + LS;
    }

    /** Returns string for showing 'help' command usage instruction */
    private static String getUsageInfoForHelpCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_HELP_WORD, COMMAND_HELP_DESC) + LS
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_HELP_EXAMPLE);
    }

    /** Returns the string for showing 'exit' command usage instruction */
    private static String getUsageInfoForExitCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_EXIT_WORD, COMMAND_EXIT_DESC) + LS
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_EXIT_EXAMPLE) + LS;
    }


    /*
     * ============================
     *         UTILITY METHODS
     * ============================
     */

    /**
     * Removes sign(p/, d/, etc) from parameter string
     *
     * @param s  Parameter as a string
     * @param sign  Parameter sign to be removed
     * @return  string without the sign
     */
    private static String removePrefixSign(String s, String sign) {
        return s.replace(sign, "");
    }

}
