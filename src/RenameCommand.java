import java.util.List;

public class RenameCommand extends Command {
    private final String tableName;
    private final List<String> newAttrNames;

    public RenameCommand(String tableName, List<String> newAttrNames) {
        this.tableName = tableName;
        this.newAttrNames = newAttrNames;
    }

    @Override
    public void execute() {
        System.out.println("Command not yet implemented.");
    }

    @Override
    public String toString() {
        return "RenameCommand{tableName='" + tableName + "', newAttrNames=" + newAttrNames + "}";
    }
}
