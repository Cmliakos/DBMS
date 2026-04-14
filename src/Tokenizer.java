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

            if (current == ',') {
                tokens.add(new Token(TokenType.COMMA, ","));
                pos++;
                continue;
            }

            if (current == ';') {
                tokens.add(new Token(TokenType.SEMICOLON, ";"));
                pos++;
                continue;
            }

            // String literals
            if (current == '"') {
                pos++;
                int start = pos;

                while (pos < input.length() && input.charAt(pos) != '"') {
                    pos++;
                }

                if (pos >= input.length()) {
                    throw new RuntimeException("Unterminated string literal");
                }

                String value = input.substring(start, pos);
                pos++;
                tokens.add(new Token(TokenType.STRING_LITERAL, value));
                continue;
            }

            // Operators
            if (current == '=' || current == '>' || current == '<' || current == '!') {
                if (current == '=') {
                    pos++;
                    tokens.add(new Token(TokenType.OPERATOR, "="));
                    continue;
                }

                if (current == '!') {
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                        pos += 2;
                        tokens.add(new Token(TokenType.OPERATOR, "!="));
                        continue;
                    } else {
                        throw new RuntimeException("Unexpected character: " + current);
                    }
                }

                if (current == '<') {
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                        pos += 2;
                        tokens.add(new Token(TokenType.OPERATOR, "<="));
                        continue;
                    } else {
                        pos++;
                        tokens.add(new Token(TokenType.OPERATOR, "<"));
                        continue;
                    }
                }

                if (current == '>') {
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                        pos += 2;
                        tokens.add(new Token(TokenType.OPERATOR, ">="));
                        continue;
                    } else {
                        pos++;
                        tokens.add(new Token(TokenType.OPERATOR, ">"));
                        continue;
                    }
                }
            }

            if (Character.isDigit(current) ||
                    (current == '-' && pos + 1 < input.length() && Character.isDigit(input.charAt(pos + 1)))) {
                int start = pos;
                boolean hasDot = false;
                if (current == '-') pos++;

                while (pos < input.length()) {
                    char c = input.charAt(pos);
                    if (Character.isDigit(c)) {
                        pos++;
                    } else if (c == '.' && !hasDot) {
                        hasDot = true;
                        pos++;
                    } else {
                        break;
                    }
                }

                String number = input.substring(start, pos);
                if (hasDot) {
                    tokens.add(new Token(TokenType.FLOAT_LITERAL, number));
                } else {
                    tokens.add(new Token(TokenType.INTEGER_LITERAL, number));
                }
                continue;
            }

            // Words
            if (Character.isLetter(current) || current == '_') {
                int start = pos;

                while (pos < input.length()) {
                    char c = input.charAt(pos);
                    if (Character.isLetterOrDigit(c) || c == '_' || c == '.') {
                        pos++;
                    } else {
                        break;
                    }
                }

                String word = input.substring(start, pos);
                String upper = word.toUpperCase();

                if (KEYWORDS.contains(upper)) {
                    tokens.add(new Token(TokenType.KEYWORD, upper));
                } else {
                    tokens.add(new Token(TokenType.IDENTIFIER, word));
                }

                continue;
            }

            throw new RuntimeException("Unexpected character: " + current);
        }

        tokens.add(new Token(TokenType.EOF, "EOF"));
        return tokens;
    }
}