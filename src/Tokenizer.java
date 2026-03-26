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

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char current = input.charAt(pos);

            // Skip whitespace
            if (Character.isWhitespace(current)) {
                pos++;
                continue;
            }

            if (current == '(') {
                tokens.add(new Token(TokenType.LPAREN, "("));
                pos++;
                continue;
            }

            if (current == ')') {
                tokens.add(new Token(TokenType.RPAREN, ")"));
                pos++;
                continue;
            }

            if (current == '(') {
                tokens.add(new Token(TokenType.LPAREN, "("));
                pos++;
                continue;
            }

            if (current == ';') {
                tokens.add(new Token(TokenType.SEMICOLON, ";"));
                pos++;
                continue;
            }

            // String literals
            
        }

        tokens.add(new Token(TokenType.EOF, "EOF"));
        return tokens;
    }
}
