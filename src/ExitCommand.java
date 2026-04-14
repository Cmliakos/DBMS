public class ExitCommand extends Command {
    @Override
    public String toString() {
        return "ExitCommand";
    }

    @Override
    public void execute() {
        try {
            Main.saveSession();
            System.out.println("All data saved. Exiting program.");
            Main.scanner.close();
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Could not exit cleanly: " + e.getMessage());
        }
    }
}