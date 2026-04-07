import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BST implements Serializable {
    static final long serialVersionUID = 1L;

    private BSTNode root;
    private final String keyType; // "INTEGER", "FLOAT", "TEXT"

    public BST(String keyType) {
        this.keyType = keyType;
        this.root = null;
    }

    private int compareKeys(String a, String b) {
        if (keyType.equals("INTEGER")) {
            return Long.compare(Long.parseLong(a), Long.parseLong(b));
        } else if (keyType.equals("FLOAT")) {
            return Double.compare(Double.parseDouble(a), Double.parseDouble(b));
        } else {
            return a.compareToIgnoreCase(b);
        }
    }

    public boolean insert(String key, long offset) {
        if (root == null) {
            root = new BSTNode(key, offset);
            return true;
        }
        BSTNode current = root;
        while (true) {
            int cmp = compareKeys(key, current.key);
            if (cmp == 0) {
                return false;
            } else if (cmp < 0) {
                if (current.left == null) {
                    current.left = new BSTNode(key, offset);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new BSTNode(key, offset);
                    return true;
                }
                current = current.right;
            }
        }
    }

    public long search(String key) {
        BSTNode current = root;
        while (current != null) {
            int cmp = compareKeys(key, current.key);
            if (cmp == 0) return current.offset;
            current = cmp < 0 ? current.left : current.right;
        }
        return -1;
    }

    public List<Long> inOrderOffsets() {
        List<Long> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(BSTNode node, List<Long> result) {
        if (node == null) return;
        inOrder(node.left, result);
        result.add(node.offset);
        inOrder(node.right, result);
    }

    public void save(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        }
    }

    public static BST load(File file, String keyType) throws IOException, ClassNotFoundException {
        if (!file.exists()) {
            return new BST(keyType);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (BST) ois.readObject();
        }
    }
}
