import java.io.*;
import java.util.*;

public class DescribeCommand extends Command {
    private final boolean describeAll;
    private final String tableName;

    public DescribeCommand(boolean describeAll, String tableName) {
        this.describeAll = describeAll;
        this.tableName = tableName;
    }

    public boolean isDescribeAll() { return describeAll; }
    public String getTableName() { return tableName; }

    @Override
    public void execute() {
        if (Main.currentDatabase == null) {
            System.out.println("Error: No database selected. Use 'USE <dbname>;' first.");
            return;
        }
        File dbDir = new File("../data/" + Main.currentDatabase);
        if (describeAll) {
            File[] tableFiles = dbDir.listFiles((dir, name) -> name.endsWith(".tbl"));
            if (tableFiles == null || tableFiles.length == 0) {
                System.out.println("No tables in database '" + Main.currentDatabase + "'.");
                return;
            }
            Arrays.sort(tableFiles);
            for (File f : tableFiles) {
                printSchema(f.getName().replace(".tbl", ""), f);
            }
        } else {
            File tableFile = new File(dbDir, tableName + ".tbl");
            if (!tableFile.exists()) {
                System.out.println("Error: Table '" + tableName + "' does not exist.");
                return;
            }
            printSchema(tableName, tableFile);
        }
    }

    private void printSchema(String name, File tableFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(tableFile))) {
            String firstLine = reader.readLine();
            if (firstLine == null || !firstLine.startsWith("COLUMNS")) {
                System.out.println("Error: Corrupt table file: " + tableFile.getName());
                return;
            }
            int numColumns = Integer.parseInt(firstLine.split(" ")[1]);
            System.out.println(name.toUpperCase());
            for (int i = 0; i < numColumns; i++) {
                String line = reader.readLine();
                if (line == null) break;
                String[] parts = line.split(" ");
                String colName = parts[0].toUpperCase();
                String type = parts[1].charAt(0) + parts[1].substring(1).toLowerCase();
                boolean pk = parts.length == 3 && parts[2].equals("PK");
                System.out.println(colName + ": " + type + (pk ? " PRIMARY KEY" : ""));
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read table file: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "DescribeCommand{describeAll=" + describeAll + ", tableName='" + tableName + "'}";
    }
}