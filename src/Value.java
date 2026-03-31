public class Value {
    private final String type;
    private final String value;

    public Value(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() { return type; }
    public String getValue() { return value; }

    @Override
    public String toString() {
        return "Value{type='" + type + "', value='" + value + "'}";
    }
}