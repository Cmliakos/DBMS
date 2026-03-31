import java.io.File;


public class UseCommand extends Command {
    private final String databaseName;

    public UseCommand(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() { return databaseName; }

    @Override
    public void execute() {
        // check if directory exists then set currentDatabase if it does
        File dbDirectory = new File("../data/" + databaseName);
        if (dbDirectory.exists() && dbDirectory.isDirectory()) {
            Main.currentDatabase = databaseName;
            System.out.println("Using database '" + databaseName + "'.");
        } else {
            System.out.println("Error: Database '" + databaseName + "' does not exist.");
        }
    }

    @Override
    public String toString() {
        return "UseCommand{databaseName='" + databaseName + "'}";
    }
}