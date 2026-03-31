import java.util.List;

public class ParserTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testCreateDatabase();
        testUse();
        testCreateTableNoPrimaryKey();
        testCreateTableWithPrimaryKey();
        testDescribeAll();
        testDescribeTable();
        testInsert();
        testUpdateSingleSet();
        testUpdateMultipleSet();
        testUpdateWithWhere();
        testSelectSingleColumn();
        testSelectMultipleColumns();
        testSelectMultipleTables();
        testSelectWithWhere();
        testDeleteNoWhere();
        testDeleteWithWhere();
        testRename();
        testLet();
        testInputNoOutput();
        testInputWithOutput();
        testExit();
        testCaseInsensitiveKeywords();
        testCaseInsensitiveIdentifiers();
        testMultiLineStatement();
        testConditionWithAnd();
        testConditionWithOr();
        testConditionLessThan();
        testConditionLessThanOrEqual();
        testConditionGreaterThanOrEqual();
        testConditionNotEqual();
        testConditionAttrToAttr();
        testConditionThreePredicates();

        System.out.println("\n--- Results: " + passed + " passed, " + failed + " failed ---");
    }

    // CREATE DATABASE
    private static void testCreateDatabase() {
        Command cmd = parse("CREATE DATABASE mydb;");
        assertTrue("CREATE DATABASE returns CreateDatabaseCommand",
                cmd instanceof CreateDatabaseCommand);
        assertEquals("CREATE DATABASE name", "mydb",
                ((CreateDatabaseCommand) cmd).getDatabaseName());
    }

    // USE
    private static void testUse() {
        Command cmd = parse("USE mydb;");
        assertTrue("USE returns UseCommand", cmd instanceof UseCommand);
        assertEquals("USE db name", "mydb", ((UseCommand) cmd).getDatabaseName());
    }

    // CREATE TABLE
    private static void testCreateTableNoPrimaryKey() {
        Command cmd = parse("CREATE TABLE employee (name text, salary float);");
        assertTrue("CREATE TABLE returns CreateTableCommand", cmd instanceof CreateTableCommand);
        CreateTableCommand c = (CreateTableCommand) cmd;
        assertEquals("table name", "employee", c.getTableName());
        assertEquals("column count", 2, c.getColumns().size());
        assertEquals("col1 name", "name", c.getColumns().get(0).getName());
        assertEquals("col1 type", "TEXT", c.getColumns().get(0).getType());
        assertFalse("col1 not PK", c.getColumns().get(0).isPrimaryKey());
        assertEquals("col2 type", "FLOAT", c.getColumns().get(1).getType());
    }

    private static void testCreateTableWithPrimaryKey() {
        Command cmd = parse("CREATE TABLE student (id integer primary key, name text);");
        CreateTableCommand c = (CreateTableCommand) cmd;
        assertTrue("first col is PK", c.getColumns().get(0).isPrimaryKey());
        assertFalse("second col is not PK", c.getColumns().get(1).isPrimaryKey());
        assertEquals("PK col name", "id", c.getColumns().get(0).getName());
        assertEquals("PK col type", "INTEGER", c.getColumns().get(0).getType());
    }

    // DESCRIBE
    private static void testDescribeAll() {
        Command cmd = parse("DESCRIBE ALL;");
        assertTrue("DESCRIBE ALL returns DescribeCommand", cmd instanceof DescribeCommand);
        assertTrue("describeAll flag", ((DescribeCommand) cmd).isDescribeAll());
    }

    private static void testDescribeTable() {
        Command cmd = parse("DESCRIBE student;");
        DescribeCommand c = (DescribeCommand) cmd;
        assertFalse("not describeAll", c.isDescribeAll());
        assertEquals("table name", "student", c.getTableName());
    }

    // INSERT
    private static void testInsert() {
        Command cmd = parse("INSERT student VALUES (1, \"John\", 3.5);");
        assertTrue("INSERT returns InsertCommand", cmd instanceof InsertCommand);
        InsertCommand c = (InsertCommand) cmd;
        assertEquals("table name", "student", c.getTableName());
        assertEquals("value count", 3, c.getValues().size());
        assertEquals("val1 type", "INTEGER", c.getValues().get(0).getType());
        assertEquals("val1 value", "1", c.getValues().get(0).getValue());
        assertEquals("val2 type", "TEXT", c.getValues().get(1).getType());
        assertEquals("val3 type", "FLOAT", c.getValues().get(2).getType());
    }

    // UPDATE
    private static void testUpdateSingleSet() {
        Command cmd = parse("UPDATE student SET name = \"Jane\";");
        assertTrue("UPDATE returns UpdateCommand", cmd instanceof UpdateCommand);
        UpdateCommand c = (UpdateCommand) cmd;
        assertEquals("table name", "student", c.getTableName());
        assertEquals("column count", 1, c.getColumnNames().size());
        assertEquals("col name", "name", c.getColumnNames().get(0));
        assertEquals("new value", "Jane", c.getNewValues().get(0).getValue());
        assertNull("no condition", c.getCondition());
    }

    private static void testUpdateMultipleSet() {
        Command cmd = parse("UPDATE student SET name = \"Jane\", gpa = 4.0;");
        UpdateCommand c = (UpdateCommand) cmd;
        assertEquals("two assignments", 2, c.getColumnNames().size());
        assertEquals("second col", "gpa", c.getColumnNames().get(1));
        assertEquals("second val type", "FLOAT", c.getNewValues().get(1).getType());
    }

    private static void testUpdateWithWhere() {
        Command cmd = parse("UPDATE student SET gpa = 4.0 WHERE id = 1;");
        UpdateCommand c = (UpdateCommand) cmd;
        assertNotNull("has condition", c.getCondition());
        assertEquals("condition attr", "id", c.getCondition().leftAttrs.get(0));
        assertEquals("condition op", "=", c.getCondition().ops.get(0));
        assertEquals("condition val", "1", c.getCondition().rights.get(0));
    }

    // SELECT
    private static void testSelectSingleColumn() {
        Command cmd = parse("SELECT name FROM student;");
        assertTrue("SELECT returns SelectCommand", cmd instanceof SelectCommand);
        SelectCommand c = (SelectCommand) cmd;
        assertEquals("one attr", 1, c.getAttrNames().size());
        assertEquals("attr name", "name", c.getAttrNames().get(0));
        assertEquals("one table", 1, c.getTableNames().size());
        assertEquals("table name", "student", c.getTableNames().get(0));
        assertNull("no condition", c.getCondition());
    }

    private static void testSelectMultipleColumns() {
        Command cmd = parse("SELECT name, gpa FROM student;");
        SelectCommand c = (SelectCommand) cmd;
        assertEquals("two attrs", 2, c.getAttrNames().size());
        assertEquals("second attr", "gpa", c.getAttrNames().get(1));
    }

    private static void testSelectMultipleTables() {
        Command cmd = parse("SELECT name FROM student, employee;");
        SelectCommand c = (SelectCommand) cmd;
        assertEquals("two tables", 2, c.getTableNames().size());
        assertEquals("second table", "employee", c.getTableNames().get(1));
    }

    private static void testSelectWithWhere() {
        Command cmd = parse("SELECT name FROM student WHERE gpa > 3.5;");
        SelectCommand c = (SelectCommand) cmd;
        assertNotNull("has condition", c.getCondition());
        assertEquals("condition op", ">", c.getCondition().ops.get(0));
    }

    // DELETE
    private static void testDeleteNoWhere() {
        Command cmd = parse("DELETE student;");
        assertTrue("DELETE returns DeleteCommand", cmd instanceof DeleteCommand);
        DeleteCommand c = (DeleteCommand) cmd;
        assertEquals("table name", "student", c.getTableName());
        assertNull("no condition", c.getCondition());
    }

    private static void testDeleteWithWhere() {
        Command cmd = parse("DELETE student WHERE id = 5;");
        DeleteCommand c = (DeleteCommand) cmd;
        assertNotNull("has condition", c.getCondition());
        assertEquals("condition attr", "id", c.getCondition().leftAttrs.get(0));
    }

    // RENAME
    private static void testRename() {
        Command cmd = parse("RENAME student (studentid, fullname, gpa);");
        assertTrue("RENAME returns RenameCommand", cmd instanceof RenameCommand);
        RenameCommand c = (RenameCommand) cmd;
        assertEquals("table name", "student", c.getTableName());
        assertEquals("three new names", 3, c.getNewAttrNames().size());
        assertEquals("first new name", "studentid", c.getNewAttrNames().get(0));
        assertEquals("third new name", "gpa", c.getNewAttrNames().get(2));
    }

    // LET
    private static void testLet() {
        Command cmd = parse("LET result KEY id SELECT name FROM student;");
        assertTrue("LET returns LetCommand", cmd instanceof LetCommand);
        LetCommand c = (LetCommand) cmd;
        assertEquals("table name", "result", c.getTableName());
        assertEquals("key attr", "id", c.getKeyAttr());
        assertNotNull("has select", c.getSelect());
        assertEquals("select attr", "name", c.getSelect().getAttrNames().get(0));
        assertEquals("select table", "student", c.getSelect().getTableNames().get(0));
    }

    // INPUT
    private static void testInputNoOutput() {
        Command cmd = parse("INPUT a.dat;");
        assertTrue("INPUT returns InputCommand", cmd instanceof InputCommand);
        InputCommand c = (InputCommand) cmd;
        assertEquals("input file", "a.dat", c.getInputFile());
        assertNull("no output file", c.getOutputFile());
    }

    private static void testInputWithOutput() {
        Command cmd = parse("INPUT a.dat OUTPUT b.dat;");
        InputCommand c = (InputCommand) cmd;
        assertEquals("input file", "a.dat", c.getInputFile());
        assertEquals("output file", "b.dat", c.getOutputFile());
    }

    // EXIT
    private static void testExit() {
        Command cmd = parse("EXIT;");
        assertTrue("EXIT returns ExitCommand", cmd instanceof ExitCommand);
    }

    // Case insensitivity
    private static void testCaseInsensitiveKeywords() {
        Command cmd = parse("create database mydb;");
        assertTrue("lowercase keywords parsed", cmd instanceof CreateDatabaseCommand);

        cmd = parse("Create Database mydb;");
        assertTrue("mixed case keywords parsed", cmd instanceof CreateDatabaseCommand);
    }

    private static void testCaseInsensitiveIdentifiers() {
        Command cmd = parse("USE MyDB;");
        assertEquals("identifier lowercased", "mydb", ((UseCommand) cmd).getDatabaseName());

        cmd = parse("USE MYDB;");
        assertEquals("uppercase identifier lowercased", "mydb", ((UseCommand) cmd).getDatabaseName());
    }

    // Multi-line statement
    private static void testMultiLineStatement() {
        Command cmd = parse("USE\nMYDB\n;");
        assertTrue("multi-line USE parsed", cmd instanceof UseCommand);
        assertEquals("db name", "mydb", ((UseCommand) cmd).getDatabaseName());
    }

    // Compound conditions
    private static void testConditionWithAnd() {
        Command cmd = parse("SELECT name FROM student WHERE gpa > 3.0 AND id = 1;");
        SelectCommand c = (SelectCommand) cmd;
        assertEquals("two predicates", 2, c.getCondition().leftAttrs.size());
        assertEquals("connective is AND", "AND", c.getCondition().connectives.get(0));
    }

    private static void testConditionWithOr() {
        Command cmd = parse("DELETE student WHERE id = 1 OR id = 2;");
        DeleteCommand c = (DeleteCommand) cmd;
        assertEquals("connective is OR", "OR", c.getCondition().connectives.get(0));
    }

    private static void testConditionLessThan() {
        Command cmd = parse("SELECT name FROM student WHERE gpa < 2.0;");
        SelectCommand c = (SelectCommand) cmd;
        assertEquals("op is <", "<", c.getCondition().ops.get(0));
    }

    private static void testConditionLessThanOrEqual() {
        Command cmd = parse("SELECT name FROM student WHERE gpa <= 2.0;");
        SelectCommand c = (SelectCommand) cmd;
        assertEquals("op is <=", "<=", c.getCondition().ops.get(0));
    }

    private static void testConditionGreaterThanOrEqual() {
        Command cmd = parse("SELECT name FROM student WHERE gpa >= 3.5;");
        SelectCommand c = (SelectCommand) cmd;
        assertEquals("op is >=", ">=", c.getCondition().ops.get(0));
    }

    private static void testConditionNotEqual() {
        Command cmd = parse("SELECT name FROM student WHERE id != 99;");
        SelectCommand c = (SelectCommand) cmd;
        assertEquals("op is !=", "!=", c.getCondition().ops.get(0));
    }

    private static void testConditionAttrToAttr() {
        Command cmd = parse("SELECT name FROM student WHERE id = otherid;");
        SelectCommand c = (SelectCommand) cmd;
        assertEquals("right side is attr name", "otherid", c.getCondition().rights.get(0));
        assertTrue("rightIsAttr is true", c.getCondition().rightIsAttrs.get(0));
    }

    private static void testConditionThreePredicates() {
        Command cmd = parse("SELECT name FROM student WHERE gpa > 3.0 AND id != 5 OR name = \"John\";");
        SelectCommand c = (SelectCommand) cmd;
        assertEquals("three predicates", 3, c.getCondition().leftAttrs.size());
        assertEquals("first connective AND", "AND", c.getCondition().connectives.get(0));
        assertEquals("second connective OR", "OR", c.getCondition().connectives.get(1));
    }

    // Helpers
    private static Command parse(String input) {
        Tokenizer tokenizer = new Tokenizer(input);
        List<Token> tokens = tokenizer.tokenize();
        return new Parser(tokens).parseStatement();
    }

    private static void assertTrue(String name, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + name);
            passed++;
        } else {
            System.out.println("FAIL: " + name);
            failed++;
        }
    }

    private static void assertFalse(String name, boolean condition) {
        assertTrue(name, !condition);
    }

    private static void assertEquals(String name, Object expected, Object actual) {
        if (expected == null ? actual == null : expected.equals(actual)) {
            System.out.println("PASS: " + name);
            passed++;
        } else {
            System.out.println("FAIL: " + name + " — expected: " + expected + ", got: " + actual);
            failed++;
        }
    }

    private static void assertNull(String name, Object obj) {
        assertTrue(name + " is null", obj == null);
    }

    private static void assertNotNull(String name, Object obj) {
        assertTrue(name + " is not null", obj != null);
    }
}
