import java.util.List;

public class InsertCommand extends Command {
    private final String tableName;
    private final List<Value> values;

    public InsertCommand(String tableName, List<Value> values) {
        this.tableName = tableName;
        this.values = values;
    }

    public String getTableName() { return tableName; }
    public List<Value> getValues() { return values; }

    @Override
    public String toString() {
        return "InsertCommand{tableName='" + tableName + "', values=" + values + "}";
    }
}