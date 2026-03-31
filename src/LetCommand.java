public class LetCommand extends Command {
    private final String tableName;
    private final String keyAttr;
    private final SelectCommand select;

    public LetCommand(String tableName, String keyAttr, SelectCommand select) {
        this.tableName = tableName;
        this.keyAttr = keyAttr;
        this.select = select;
    }

    public String getTableName() { return tableName; }
    public String getKeyAttr() { return keyAttr; }
    public SelectCommand getSelect() { return select; }

    @Override
    public String toString() {
        return "LetCommand{tableName='" + tableName + "', keyAttr='" + keyAttr + "', select=" + select + "}";
    }
}
