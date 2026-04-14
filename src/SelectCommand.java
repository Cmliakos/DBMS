import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.*;


public class SelectCommand extends Command {
    private final List<String> attrNames;
    private final List<String> tableNames;
    private final Condition condition; // null if no WHERE clause

    public SelectCommand(List<String> attrNames, List<String> tableNames, Condition condition) {
        this.attrNames = attrNames;
        this.tableNames = tableNames;
        this.condition = condition;
    }

    public List<String> getAttrNames() { return attrNames; }
    public List<String> getTableNames() { return tableNames; }
    public Condition getCondition() { return condition; }

    private boolean matches(String[] rowValues, Condition condition, Map<String, Integer> colToIndex, List<String> colTypes) {
        if (condition == null) return true;
        if (condition.leftAttrs.isEmpty()) return true;

        boolean result = evaluatePredicate(0, rowValues, condition, colToIndex, colTypes);
        for (int i = 1; i < condition.leftAttrs.size(); i++) {
            boolean next = evaluatePredicate(i, rowValues, condition, colToIndex, colTypes);
            String connective = condition.connectives.get(i - 1);
            if ("AND".equalsIgnoreCase(connective)) {
                result = result && next;
            } else if ("OR".equalsIgnoreCase(connective)) {
                result = result || next;
            } else {
                result = false;
            }
        }
        return result;
    }

    private boolean evaluatePredicate(int i, String[] rowValues, Condition condition, Map<String, Integer> colToIndex, List<String> colTypes) {
        String leftAttr = condition.leftAttrs.get(i).toLowerCase();
        if (!colToIndex.containsKey(leftAttr)) return false;
        int leftIndex = colToIndex.get(leftAttr);
        String leftStr = rowValues[leftIndex];

        String rightStr;
        if (condition.rightIsAttrs.get(i)) {
            String rightAttr = condition.rights.get(i).toLowerCase();
            if (!colToIndex.containsKey(rightAttr)) return false;
            int rightIndex = colToIndex.get(rightAttr);
            rightStr = rowValues[rightIndex];
        } else {
            rightStr = condition.rights.get(i);
        }

        String op = condition.ops.get(i);
        String type = colTypes.get(leftIndex);

        boolean match = false;
        if (type.equals("INT")) {
            try {
                int left = Integer.parseInt(leftStr.trim());
                int right = Integer.parseInt(rightStr.trim());
                match = compareInts(left, right, op);
            } catch (NumberFormatException e) {
                match = false;
            }
        } else {
            match = compareStrings(leftStr, rightStr, op);
        }
        return match;
    }

    private boolean compareInts(int left, int right, String op) {
        switch (op) {
            case "=": return left == right;
            case "!=": return left != right;
            case "<": return left < right;
            case ">": return left > right;
            case "<=": return left <= right;
            case ">=": return left >= right;
            default: return false;
        }
    }

    private boolean compareStrings(String left, String right, String op) {
        int cmp = left.compareTo(right);
        switch (op) {
            case "=": return cmp == 0;
            case "!=": return cmp != 0;
            case "<": return cmp < 0;
            case ">": return cmp > 0;
            case "<=": return cmp <= 0;
            case ">=": return cmp >= 0;
            default: return false;
        }
    }

    private String getSearchablePKValue(Condition condition, String pkName) {
        if (condition == null) return null;
        
        for (int i = 0; i < condition.leftAttrs.size(); i++) {
            // if the left side is our PK and the operator is "=", we can use the BST
            if (condition.leftAttrs.get(i).equalsIgnoreCase(pkName) && condition.ops.get(i).equals("=")) {
                return condition.rights.get(i);
            }
        }
        return null;
    }

    @Override
    public void execute() {
        // identify the source and schema 
        // locate the file
        File tableFile = new File("../data/" + Main.currentDatabase + "/" + tableNames.get(0) + ".tbl");

        // load metadata
        Map<String, Integer> colToIndex = new HashMap<>();
        List<String> colTypes = new ArrayList<>();
        int pkIndex = -1;
        String pkName = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(tableFile))) {
            String line = reader.readLine();
            if (line != null && line.startsWith("COLUMNS")) {
                int numCols = Integer.parseInt(line.split(" ")[1]);
                for (int i = 0; i < numCols; i++) {
                    String colDef = reader.readLine();
                    String[] parts = colDef.split(" ");
                    String name = parts[0].toLowerCase();
                    String type = parts[1].toUpperCase();

                    colToIndex.put(name, i);
                    colTypes.add(type);
                    if (parts.length == 3 && parts[2].equals("PK")) {
                        pkIndex = i;
                        pkName = name;
                    }
                }

                String targetPkValue = getSearchablePKValue(condition, pkName);

                if (targetPkValue != null) {
                    File indexFile = new File("../data/" + Main.currentDatabase + "/" + tableNames.get(0) + ".idx");
                    BST bst = BST.load(indexFile, colTypes.get(pkIndex)); 
                    long offset = bst.search(targetPkValue);
                    
                    if (offset >= 0) {
                        try (RandomAccessFile raf = new RandomAccessFile(tableFile, "r")) {
                            raf.seek(offset);
                            String row = raf.readLine();
                            if (row != null) {
                                String[] rowValues = row.split("\\|");
                                if (condition == null || matches(rowValues, condition, colToIndex, colTypes)) {
                                    System.out.println(String.join("|", rowValues));
                                }
                            }
                        }
                    }

                } else {
                    System.out.println("Using Full Table Scan...");
                        
                    if ((line = reader.readLine()) != null && line.startsWith("DATA")) {
                        while ((line = reader.readLine()) != null) {
                            // split the line
                            String[] rowValues = line.split("\\|");

                            // filter 
                            if (condition == null || matches(rowValues, condition, colToIndex, colTypes)) {
                                System.out.println(String.join("|", rowValues));
                            }
                        }
                    } else {
                        System.out.println("Error: Expected DATA as next line in table file");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading table file: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "SelectCommand{attrNames=" + attrNames + ", tableNames=" + tableNames
                + ", condition=" + condition + "}";
    }
}
