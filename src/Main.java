import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Store current database in a file to persist across sessions
    public static String currentDatabase = null;
    // Scanner for user input
    public static Scanner scanner;

    // Session file to store the current database name across runs
    private static final File SESSION_FILE = new File("../data/.session");

    // Load the last used database from the session file, if it exists
    private static void loadSession() {
        if (!SESSION_FILE.exists()) return;
        // Read the database name from the session file and set currentDatabase accordingly
        try (BufferedReader r = new BufferedReader(new FileReader(SESSION_FILE))) {
            // The session file is expected to contain a single line with the database name
            String db = r.readLine();
            // 
            if (db != null && !db.isBlank()) {
                File dbDir = new File("../data/" + db.trim());
                if (dbDir.exists() && dbDir.isDirectory()) {
                    currentDatabase = db.trim();
                    // Inform the user that the session has been restored
                    System.out.println("Restored session: using database '" + currentDatabase + "'.");
                }
            }
        } catch (IOException e) {
            // ignore — start fresh
        }
    }

    // Save the current database to the session file for future runs
    public static void saveSession() {
        // Write the current database name to the session file so it can be restored in the next run
        try {
            SESSION_FILE.getParentFile().mkdirs();
            // If currentDatabase is null, we write an empty file to clear the session
            try (PrintWriter w = new PrintWriter(new FileWriter(SESSION_FILE))) {
                // If currentDatabase is not null, write its name to the session file; otherwise, write nothing to clear the session
                if (currentDatabase != null) w.println(currentDatabase);
            }
        } catch (IOException e) {
            // If we can't save the session, we just ignore it and start fresh next time
            System.out.println("Warning: could not save session: " + e.getMessage());
        }
    }

    // Main loop of the program: read user input, parse commands, and execute them
    public static void main(String[] args) {
        // Load the last used database from the session file, if it exists, so we can restore the previous session
        loadSession();
        // Initialize the scanner and the input buffer for reading user commands
        scanner = new Scanner(System.in);
        StringBuilder buffer = new StringBuilder();

        // Print a welcome message to the user when the program starts
        System.out.println("DBMS started. Type EXIT; to quit.");

        // Main loop: read user input, parse commands, and execute them
        while (true) {
            // Read a line of input from the user and append it to the buffer
            String line = scanner.nextLine();
            buffer.append(line).append("\n");

            // Check if the buffer contains a complete command (terminated by a semicolon)
            while (buffer.indexOf(";") != -1) {
                int semicolonIndex = buffer.indexOf(";");

                // Extract the command from the buffer up to and including the semicolon, and trim any leading/trailing whitespace
                String statement = buffer.substring(0, semicolonIndex + 1).trim();

                // Remove the processed command from the buffer so we can read the next command
                buffer.delete(0, semicolonIndex + 1);

                // Tokenizer and parser to convert the command string into a Command object
                try {
                    // Tokenize the command string into a list of tokens
                    Tokenizer tokenizer = new Tokenizer(statement);
                    List<Token> tokens = tokenizer.tokenize();
                    // Parse the list of tokens into a Command object that can be executed
                    Parser parser = new Parser(tokens);
                    Command command = parser.parseStatement();
                    // Execute the command, which will perform the desired action and print any results or errors
                    command.execute();
                } catch (RuntimeException e) {
                    // If there is an error during tokenization, parsing, or execution of the command, print the error message to the user
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}