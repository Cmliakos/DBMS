import java.util.Scanner;

public class Main {
    
   public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        StringBuilder buffer = new StringBuilder();

        System.out.println("DBMS started. Type EXIT; to quit.");

        while (true) {
            String line = scanner.nextLine();
            buffer.append(line).append(" ");

            if (buffer.toString().contains(";")) {

                String statement = buffer.toString().trim();
                buffer.setLength(0);

                System.out.println("Received statement: " + statement);

                if (statement.equalsIgnoreCase("EXIT;")) {
                    System.out.println("Exiting DBMS.");
                    break;
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

        scanner.close();
   }
}