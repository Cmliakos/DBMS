import java.io.*;
import java.util.*;

public class RenameCommand extends Command {
    private final String tableName;
    private final List<String> newAttrNames;

    public RenameCommand(String tableName, List<String> newAttrNames) {
        this.tableName = tableName;
        this.newAttrNames = newAttrNames;
    }

    public String getTableName() { return tableName; }
    public List<String> getNewAttrNames() { return newAttrNames; }

    @Override
    public void execute() {
        if (Main.currentDatabase == null) {
            System.out.println("Error: No database selected. Use 'USE <dbname>;' first.");
            return;
        }
        File tableFile = new File("data/" + Main.currentDatabase + "/" + tableName + ".tbl");
        if (!tableFile.exists()) {
            System.out.println("Error: Table '" + tableName + "' does not exist.");
            return;
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(tableFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read table file: " + e.getMessage());
            return;
        }

        if (lines.isEmpty() || !lines.get(0).startsWith("COLUMNS")) {
            System.out.println("Error: Corrupt table file.");
            return;
        }
        int numColumns = Integer.parseInt(lines.get(0).split(" ")[1]);

        if (newAttrNames.size() != numColumns) {
            System.out.println("Error: RENAME expects " + numColumns + " attribute name(s), but got " + newAttrNames.size() + ".");
            return;
        }

        for (int i = 0; i < numColumns; i++) {
            String[] parts = lines.get(i + 1).split(" ");
            StringBuilder newLine = new StringBuilder();
            newLine.append(newAttrNames.get(i).toLowerCase());
            newLine.append(" ").append(parts[1]);
            if (parts.length == 3 && parts[2].equals("PK")) {
                newLine.append(" PK");
            }
            lines.set(i + 1, newLine.toString());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(tableFile))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error: Could not write table file: " + e.getMessage());
            return;
        }

        System.out.println("Table '" + tableName + "' attributes renamed.");
    }

    @Override
    public String toString() {
        return "RenameCommand{tableName='" + tableName + "', newAttrNames=" + newAttrNames + "}";
    }
}
