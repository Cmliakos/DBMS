import java.io.File;

public class CreateDatabaseCommand extends Command {
    private final String databaseName;

    public CreateDatabaseCommand(String databaseName) {
        this.databaseName = databaseName;
    }

     @Override
    public void execute() {
        // create a folder
        // TODO fix directory not being created under data folder
        File databaseDirectory = new File("../data/" + databaseName);

        // handle if file exists
        if (databaseDirectory.exists()) {
            System.out.println("Error: Database '" + databaseName + "' already exists.");
        } else {
            if (databaseDirectory.mkdirs()) {
                System.out.println("Database created at: " + databaseDirectory.getPath());
            } else {
                System.out.println("Error: Could not create database '" + databaseName + "'.");
            }
        }
    }                 

    @Override
    public String toString() {
        return "CreateDatabaseCommand{databaseName='" + databaseName + "'}";
    }                                                                                                                                                    
}