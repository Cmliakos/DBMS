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

                String statement = buffer.toString();
                buffer.setLength(0);

                statement = statement.trim();

                System.out.println("Received statement: " + statement);

                if (statement.equalsIgnoreCase("EXIT;")) {
                    System.out.println("Exiting DBMS.");
                    break;
                }
            }
        }

        scanner.close();
   }
}
