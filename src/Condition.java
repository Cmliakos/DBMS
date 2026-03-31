import java.util.List;

public class Condition {
    public final List<String> leftAttrs;
    public final List<String> ops;
    public final List<String> rights;
    public final List<Boolean> rightIsAttrs;
    public final List<String> connectives; // "AND" or "OR" between predicates

    public Condition(List<String> leftAttrs, List<String> ops, List<String> rights,
                     List<Boolean> rightIsAttrs, List<String> connectives) {
        this.leftAttrs = leftAttrs;
        this.ops = ops;
        this.rights = rights;
        this.rightIsAttrs = rightIsAttrs;
        this.connectives = connectives;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < leftAttrs.size(); i++) {
            if (i > 0) sb.append(" ").append(connectives.get(i - 1)).append(" ");
            sb.append(leftAttrs.get(i)).append(" ").append(ops.get(i)).append(" ").append(rights.get(i));
        }
        return sb.toString();
    }
}
