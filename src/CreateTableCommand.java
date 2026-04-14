import java.io.*;
import java.util.List;

public class CreateTableCommand extends Command {
    private final String tableName;
    private final List<ColumnDef> columns;

    public CreateTableCommand(String tableName, List<ColumnDef> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public String getTableName() { return tableName; }
    public List<ColumnDef> getColumns() { return columns; }

    @Override
    public void execute() {
        if (Main.currentDatabase == null) {
            System.out.println("Error: No database selected. Use 'USE <dbname>;' first.");
            return;
        }
        File tableFile = new File("data/" + Main.currentDatabase + "/" + tableName + ".tbl");
        if (tableFile.exists()) {
            System.out.println("Error: Table '" + tableName + "' already exists.");
            return;
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(tableFile))) {
            writer.println("COLUMNS " + columns.size());
            for (ColumnDef col : columns) {
                if (col.isPrimaryKey()) {
                    writer.println(col.getName() + " " + col.getType() + " PK");
                } else {
                    writer.println(col.getName() + " " + col.getType());
                }
            }
            writer.println("DATA");
            System.out.println("Table '" + tableName + "' created.");
        } catch (IOException e) {
            System.out.println("Error: Could not create table file: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "CreateTableCommand{tableName='" + tableName + "', columns=" + columns + '}';
    }
}
