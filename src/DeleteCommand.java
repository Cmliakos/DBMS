public class DeleteCommand extends Command {
    private final String tableName;
    private final Condition condition; // null if no WHERE clause

    public DeleteCommand(String tableName, Condition condition) {
        this.tableName = tableName;
        this.condition = condition;
    }

    public String getTableName() { return tableName; }
    public Condition getCondition() { return condition; }

    @Override
    public void execute() {
        System.out.println("Command not yet implemented.");
    }

    @Override
    public String toString() {
        return "DeleteCommand{tableName='" + tableName + "', condition=" + condition + "}";
    }
}
