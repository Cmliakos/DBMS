public class InputCommand extends Command {
    private final String inputFile;
    private final String outputFile; // null if no OUTPUT clause

    public InputCommand(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public String getInputFile() { return inputFile; }
    public String getOutputFile() { return outputFile; }

    @Override
    public void execute() {
        System.out.println("Command not yet implemented.");
    }

    @Override
    public String toString() {
        return "InputCommand{inputFile='" + inputFile + "', outputFile='" + outputFile + "'}";
    }
}
