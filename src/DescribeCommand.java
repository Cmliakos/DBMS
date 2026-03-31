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
    public String toString() {
        return "DescribeCommand{describeAll=" + describeAll + ", tableName='" + tableName + "'}";
    }
}