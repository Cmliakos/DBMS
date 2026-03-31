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
        System.out.println("Command not yet implemented.");
    }

    @Override
    public String toString() {
        return "CreateTableCommand{tableName='" + tableName + "', columns=" + columns + '}';
    }
}