import java.io.Serializable;

public class BSTNode implements Serializable {
    static final long serialVersionUID = 1L;

    String key;
    long offset;
    BSTNode left;
    BSTNode right;

    BSTNode(String key, long offset) {
        this.key = key;
        this.offset = offset;
    }
}
