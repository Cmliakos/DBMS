public class CreateDatabaseCommand extends Command {
    private final String databaseName;

    public CreateDatabaseCommand(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() { return databaseName; }

    @Override
    public String toString() {
        return "CreateDatabaseCommand{databaseName='" + databaseName + "'}";
    }
}