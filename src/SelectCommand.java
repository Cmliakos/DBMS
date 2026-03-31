import java.util.List;

public class SelectCommand extends Command {
    private final List<String> attrNames;
    private final List<String> tableNames;
    private final Condition condition; // null if no WHERE clause

    public SelectCommand(List<String> attrNames, List<String> tableNames, Condition condition) {
        this.attrNames = attrNames;
        this.tableNames = tableNames;
        this.condition = condition;
    }

    public List<String> getAttrNames() { return attrNames; }
    public List<String> getTableNames() { return tableNames; }
    public Condition getCondition() { return condition; }

    @Override
    public void execute() {
        System.out.println("Command not yet implemented.");
    }

    @Override
    public String toString() {
        return "SelectCommand{attrNames=" + attrNames + ", tableNames=" + tableNames
                + ", condition=" + condition + "}";
    }
}
