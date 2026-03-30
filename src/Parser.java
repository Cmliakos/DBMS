import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    public Command parseStatement() {
        if (matchKeyword("CREATE")) {
            return parseCreate();
        }
        if (matchKeyword("USE")) {
            return parseUse();
        }
        if (matchKeyword("DESCRIBE")) {
            return parseDescribe();
        }
        if (matchKeyword("INSERT")) {
            return parseInsert();
        }
        if (matchKeyword("UPDATE")) {
            return parseUpdate();
        }
        if (matchKeyword("SELECT")) {
            return parseSelect();
        }
        if (matchKeyword("DELETE")) {
            return parseDelete();
        }
        if (matchKeyword("RENAME")) {
            return parseRename();
        }
        if (matchKeyword("LET")) {
            return parseLet();
        }
        if (matchKeyword("INPUT")) {
            return parseInput();
        }
        if (matchKeyword("EXIT")) {
            return parseExit();
        }

        throw new RuntimeException("Unexpected token: " + peek());
    }

    private boolean matchKeyword(String keyword) {
        if (check(TokenType.KEYWORD) && peek().getValue().equalsIgnoreCase(keyword)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private void expect(TokenType type, String message) {
        if (check(type)) {
            advance();
            return;
        }
        throw new RuntimeException(message + " at token: " + peek());
    }

    private void expectKeyword(String keyword, String message) {
        if (check(TokenType.KEYWORD) && peek().getValue().equalsIgnoreCase(keyword)) {
            advance();
            return;
        }
        throw new RuntimeException(message + " at token: " + peek());
    }

    private String expectIdentifier(String message) {
        if (check(TokenType.IDENTIFIER)) {
            return advance().getValue();
        }
        throw new RuntimeException(message + " at token: " + peek());
    }

    private Command parseUse() {
        String dbName = expectIdentifier("Expected database name after USE");
        expect(TokenType.SEMICOLON, "Expected ';' after USE statement");
        return new UseCommand(dbName);
    }

    private Command parseExit() {
        expect(TokenType.SEMICOLON, "Expected ';' after EXIT");
        return new ExitCommand();
    }

}
