public class UseCommand extends Command {
    private final String databaseName;

    public UseCommand(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String toString() {
        return "UseCommand{databaseName='" + databaseName + "'}";
    }
}