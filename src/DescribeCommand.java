public class DescribeCommand extends Command {
    private final boolean describeAll;
    private final String tableName;

    public DescribeCommand(boolean describeAll, String tableName) {
        this.describeAll = describeAll;
        this.tableName = tableName;
    }

    @Override
    public void execute() {
        System.out.println("Command not yet implemented.");
    }

    @Override
    public String toString() {
        return "DescribeCommand{describeAll=" + describeAll + ", tableName='" + tableName + "'}";
    }
}