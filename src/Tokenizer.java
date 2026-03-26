import java.util.*;

public class Tokenizer {
    
    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        String[] words = {
                "CREATE", "DATABASE", "TABLE", "USE", "DESCRIBE", "ALL",
                "INSERT", "VALUES", "UPDATE", "SET", "WHERE", "SELECT",
                "FROM", "DELETE", "RENAME", "LET", "KEY", "INPUT", "OUTPUT",
                "EXIT", "PRIMARY", "INTEGER", "TEXT", "FLOAT", "AND", "OR"
        };

        for (String word : words) {
            KEYWORDS.add(word);
        }
    }
    
    private final String input;
    private int pos;

    public Tokenizer(String input) {
        this.input = input;
        this.pos = 0;
    }

    
}
