import java.util.List;

public class InsertCommand extends Command {
    private final String tableName;
    private final List<Value> values;

    public InsertCommand(String tableName, List<Value> values) {
        this.tableName = tableName;
        this.values = values;
    }

    @Override
    public void execute() {
        System.out.println("Command not yet implemented.");
    }

    @Override
    public String toString() {
        return "InsertCommand{tableName='" + tableName + "', values=" + values + "}";
    }
}