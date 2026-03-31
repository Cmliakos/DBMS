public class ColumnDef {
    private final String name;
    private final String type;
    private final boolean primaryKey;

    public ColumnDef(String name, String type, boolean primaryKey) {
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "ColumnDef{name='" + name + "', type='" + type + "', primaryKey=" + primaryKey + '}';
    }
}