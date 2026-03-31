import java.util.List;

public class UpdateCommand extends Command {
    private final String tableName;
    private final List<String> columnNames;
    private final List<Value> newValues;
    private final Condition condition; // null if no WHERE clause

    public UpdateCommand(String tableName, List<String> columnNames, List<Value> newValues, Condition condition) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.newValues = newValues;
        this.condition = condition;
    }

    public String getTableName() { return tableName; }
    public List<String> getColumnNames() { return columnNames; }
    public List<Value> getNewValues() { return newValues; }
    public Condition getCondition() { return condition; }

    @Override
    public String toString() {
        return "UpdateCommand{tableName='" + tableName + "', columnNames=" + columnNames
                + ", newValues=" + newValues + ", condition=" + condition + "}";
    }
}
