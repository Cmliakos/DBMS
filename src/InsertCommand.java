import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InsertCommand extends Command {
    private final String tableName;
    private final List<Value> values;

    public InsertCommand(String tableName, List<Value> values) {
        this.tableName = tableName;
        this.values = values;
    }

    public String getTableName() { return tableName; }
    public List<Value> getValues() { return values; }

    @Override
    public void execute() {
        if (Main.currentDatabase == null) {
            System.out.println("Error: No database selected. Use 'USE <dbname>;' first.");
            return;
        }

        File tableFile = new File("../data/" + Main.currentDatabase + "/" + tableName + ".tbl");
        if (!tableFile.exists()) {
            System.out.println("Error: Table '" + tableName + "' does not exist.");
            return;
        }

        // ------------------------------------------------------------------
        // 1. Read schema from the header
        // ------------------------------------------------------------------
        List<String> colNames = new ArrayList<>();
        List<String> colTypes = new ArrayList<>();
        int pkIndex = -1;

        try (BufferedReader reader = new BufferedReader(new FileReader(tableFile))) {
            String firstLine = reader.readLine();
            if (firstLine == null || !firstLine.startsWith("COLUMNS")) {
                System.out.println("Error: Corrupt table file for '" + tableName + "'.");
                return;
            }
            int numCols = Integer.parseInt(firstLine.trim().split("\\s+")[1]);
            for (int i = 0; i < numCols; i++) {
                String line = reader.readLine();
                if (line == null) {
                    System.out.println("Error: Corrupt table file for '" + tableName + "'.");
                    return;
                }
                String[] parts = line.trim().split("\\s+");
                colNames.add(parts[0].toLowerCase());
                colTypes.add(parts[1].toUpperCase());
                if (parts.length == 3 && parts[2].equals("PK")) {
                    pkIndex = i;
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read table file: " + e.getMessage());
            return;
        }

        // ------------------------------------------------------------------
        // 2. Arity check
        // ------------------------------------------------------------------
        if (values.size() != colNames.size()) {
            System.out.println("Error: Expected " + colNames.size() +
                    " value(s) but got " + values.size() + ".");
            return;
        }

        // ------------------------------------------------------------------
        // 3. Domain constraint – value type must be compatible with column type
        //    INTEGER fits FLOAT (widening); FLOAT does not fit INTEGER
        // ------------------------------------------------------------------
        for (int i = 0; i < colNames.size(); i++) {
            String colType  = colTypes.get(i);
            String valType  = values.get(i).getType().toUpperCase();
            String valStr   = values.get(i).getValue();

            boolean ok;
            switch (colType) {
                case "INTEGER":
                    ok = valType.equals("INTEGER");
                    if (ok) {
                        try { Long.parseLong(valStr); } catch (NumberFormatException ex) { ok = false; }
                    }
                    break;
                case "FLOAT":
                    ok = valType.equals("FLOAT") || valType.equals("INTEGER");
                    if (ok) {
                        try { Double.parseDouble(valStr); } catch (NumberFormatException ex) { ok = false; }
                    }
                    break;
                case "TEXT":
                    ok = valType.equals("TEXT");
                    break;
                default:
                    ok = false;
            }

            if (!ok) {
                System.out.println("Error: Domain constraint violation for column '" +
                        colNames.get(i) + "': expected " + colType + ", got " + valType +
                        " (" + valStr + ").");
                return;
            }
        }

        // ------------------------------------------------------------------
        // 4. Entity integrity – primary key value must not be empty
        //    (We represent NULL as empty string; reject it.)
        // ------------------------------------------------------------------
        if (pkIndex >= 0 && values.get(pkIndex).getValue().isEmpty()) {
            System.out.println("Error: Entity integrity violation: primary key '" +
                    colNames.get(pkIndex) + "' cannot be null.");
            return;
        }

        // ------------------------------------------------------------------
        // 5. Key constraint – primary key must be unique (check BST)
        // ------------------------------------------------------------------
        File indexFile = new File("../data/" + Main.currentDatabase + "/" + tableName + ".idx");
        BST bst = null;
        if (pkIndex >= 0) {
            try {
                bst = BST.load(indexFile, colTypes.get(pkIndex));
            } catch (Exception e) {
                System.out.println("Error: Could not load BST index: " + e.getMessage());
                return;
            }
            String pkValue = values.get(pkIndex).getValue();
            if (bst.search(pkValue) >= 0) {
                System.out.println("Error: Key constraint violation: duplicate primary key value '" +
                        pkValue + "' for column '" + colNames.get(pkIndex) + "'.");
                return;
            }
        }

        // ------------------------------------------------------------------
        // 6. Build the record string  (pipe-delimited)
        // ------------------------------------------------------------------
        StringBuilder recordBuilder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) recordBuilder.append("|");
            recordBuilder.append(values.get(i).getValue());
        }
        String record = recordBuilder.toString();

        // ------------------------------------------------------------------
        // 7. Append record to the table file; capture byte offset first
        // ------------------------------------------------------------------
        long offset = tableFile.length();
        try (RandomAccessFile raf = new RandomAccessFile(tableFile, "rw")) {
            raf.seek(raf.length());
            raf.writeBytes(record + "\n");
        } catch (IOException e) {
            System.out.println("Error: Could not write to table file: " + e.getMessage());
            return;
        }

        // ------------------------------------------------------------------
        // 8. Update and save BST index
        // ------------------------------------------------------------------
        if (pkIndex >= 0 && bst != null) {
            bst.insert(values.get(pkIndex).getValue(), offset);
            try {
                bst.save(indexFile);
            } catch (IOException e) {
                System.out.println("Error: Could not save BST index: " + e.getMessage());
                return;
            }
        }

        System.out.println("1 row inserted into '" + tableName + "'.");
    }

    @Override
    public String toString() {
        return "InsertCommand{tableName='" + tableName + "', values=" + values + "}";
    }
}
