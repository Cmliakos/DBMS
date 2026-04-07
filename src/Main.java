import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static String currentDatabase = null;

        private static final File SESSION_FILE = new File("../data/.session");

    private static void loadSession() {
        if (!SESSION_FILE.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(SESSION_FILE))) {
            String db = r.readLine();
            if (db != null && !db.isBlank()) {
                File dbDir = new File("../data/" + db.trim());
                if (dbDir.exists() && dbDir.isDirectory()) {
                    currentDatabase = db.trim();
                    System.out.println("Restored session: using database '" + currentDatabase + "'.");
                }
            }
        } catch (IOException e) {
            // ignore — start fresh
        }
    }

    public static void saveSession() {
        try {
            SESSION_FILE.getParentFile().mkdirs();
            try (PrintWriter w = new PrintWriter(new FileWriter(SESSION_FILE))) {
                if (currentDatabase != null) w.println(currentDatabase);
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save session: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        loadSession();
        Scanner scanner = new Scanner(System.in); //no need to close, handled in exit class
        StringBuilder buffer = new StringBuilder();

        System.out.println("DBMS started. Type EXIT; to quit.");


        while (true) {
            String line = scanner.nextLine();
            buffer.append(line).append("\n");

            while (buffer.indexOf(";") != -1) {
                int semicolonIndex = buffer.indexOf(";");

                String statement = buffer.substring(0, semicolonIndex + 1).trim();

                buffer.delete(0, semicolonIndex + 1);

                try {
                    Tokenizer tokenizer = new Tokenizer(statement);
                    List<Token> tokens = tokenizer.tokenize();
                    Parser parser = new Parser(tokens);
                    Command command = parser.parseStatement();
                    System.out.println("Parsed: " + command);
                    command.execute();
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}