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
        } if (matchKeyword("USE")) {
            return parseUse();
        } if (matchKeyword("DESCRIBE")) {
            return parseDescribe();
        } if (matchKeyword("INSERT")) {
            return parseInsert();
        } if (matchKeyword("UPDATE")) {
            return parseUpdate();
        } if (matchKeyword("SELECT")) {
            return parseSelect();
        } if (matchKeyword("DELETE")) {
            return parseDelete();
        } if (matchKeyword("RENAME")) {
            return parseRename();
        } if (matchKeyword("LET")) {
            return parseLet();
        } if (matchKeyword("INPUT")) {
            return parseInput();
        } if (matchKeyword("EXIT")) {
            return parseExit();
        }

        throw new RuntimeException("Unexpected token: " + peek());
    }
}
