import java.util.Scanner;
import java.util.List;

public class Main {

    public static String currentDatabase = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
                    if (command instanceof ExitCommand) {
                        System.out.println("Exiting DBMS.");
                        scanner.close();
                        return;
                    }
                    System.out.println("Parsed: " + command);
                    command.execute();
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}