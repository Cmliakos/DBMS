import java.util.Scanner;
import java.util.List;

public class Main {

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

                System.out.println("Received statement: " + statement);

                if (statement.equalsIgnoreCase("EXIT;")) {
                    System.out.println("Exiting DBMS.");
                    scanner.close();
                    return;
                }

                try {
                    Tokenizer tokenizer = new Tokenizer(statement);
                    List<Token> tokens = tokenizer.tokenize();

                    System.out.println("Tokens:");
                    for (Token token : tokens) {
                        System.out.println(token);
                    }
                } catch (RuntimeException e) {
                    System.out.println("Tokenizer error: " + e.getMessage());
                }
            }
        }
    }
}