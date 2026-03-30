DATABASE PROJECT – 100points
Implement a DBMS as defined below.
Commands are (Create | USE | DESCribe | SELECT | LET | INSERT | UPDATE | DELETE
| RENAME | INPUT | EXIT)
Additional commands for EACH graduate student/graduate credit: SELECT with
aggregates of count, average, min, and max.
Each command is defined below:
1.
a. CREATE DATABASE Dbname;
b. use Dbname;
2. CREATE TABLE TableName ‘(‘ AttrName Domain [PRIMARY KEY] [,AttrName Domain]*
‘)’;
Dbname  Identifier
TableName  Identifier
AttrName  Identifier
Identifier  alphabet [alphanumeric]19
Domain  DataType
DataType  Integer | Text | Float
Float  Integer [. Digit [Digit]]
Text  [Char] 100
Integer  You may assume 16 or 32-bit sized integer
Creates the given table name with the attributes and types. The first attribute
may be specified as the primary key for the table. Build a binary Search tree
with the given index. Within the file, the records do not have to be ordered on
any key field.
• If a table has a primary key, then all retrievals from the table must use
the primary key index. For example, given:
o create table student (id integer primary key, name text);
▪ find the name of the student with id 123 should be via the
BST
▪ Find the id or ids of students named John Doe. Since there
is no index on name, I still want you to use the in-order
traversal of the BST to get each record and compare the
name. By doing so, everyone would have the same sequence of
name(s) for their result which makes it easier for me to
check
• If there is no primary key for the table, then you can go through the
tuples in any order.
3. DESCRIBE (ALL | TableName) ‘;’
Displays to the screen the listed table or ALL tables and their attributes and
types. Also indicate the primary key attributes.
E.g.
STUDENT
NAME: Text
ID: Integer PRIMARY KEY
EMPLOYEE
Name: Text
SSN: Integer PRIMARY KEY
Salary: Float
4. RENAME TableName ‘(‘ AttrNameList ‘)’ ‘;’
Renames all attributes of TableName to the given AttrNameList in the order
given. Note that # of attributes in TableName must equal # of attributes in
AttrNameList.
5. INSERT TableName
VALUES ‘(‘ V1, V2, .. , Vn ‘)’ ‘;’
Checks Domain Constraints, Key Constraints and Entity Integrity Constraints
for the new tuple. If all is okay, the new tuple is inserted in TableName.
Do not worry about referential integrity constraints.
6. UPDATE TableName
SET AttrName = Constant [,AttrName = Constant]*
[WHERE Condition] ‘;’
Updates tuples from TableName that satisfy the WHERE condition to the new
SET values.
7. <SELECT-OPERATION> -> SELECT AttrNameList
FROM TableNameList
[WHERE Condition] ‘;’
AttrNameList -> AttrName [,AttrName]*
TableNameList -> TableName [,TableName]*
RelOp -> <, >, <=, >=, =, !=
Constant -> IntConst | StringConst | FloatConst
IntConst -> -231 .. 231-1
StringConst -> ‘“’ [character] 30 ‘”’
FloatConst -> IntConst [ . IntConst]
Condition -> AttrName RelOp (Constant|AttrName)
[(and|or) AttrName RelOp Constant|AttrName]2
Displays to the screen the rows (with column headers) that match the select
condition or “Nothing found” when there is no match. The rows should be
numbered e.g. 1., 2., etc.
SELECT with aggregates for EACH graduate student/graduate credit. These must be
completed separately by each graduate student and demonstrated to me separately
by each graduate student.
- SELECT [count(*)|min (AttrName)|Max ( AttrName )|Average (AttrName)]
FROM TableNameList
[WHERE Condition] ‘;’
E.g. select max(gpa) from students;
select average(gpa) from students where firstName=”John”;
It will be graded as 2 points off the student’s score from their group score
for each one of (count, min, max, average) that does not work properly.
8. LET TableName
KEY AttrName
<SELECT OPERATION>
Stores the result of the SELECT operation under the given TableName with
AttrName as key. Note that this involves creating a BST based on the key for
TableName. Key AttrName must be one of the selected attributes, otherwise give
an error message and abort the query.
9. DELETE TableName [WHERE Condition] ‘;’
Deletes tuples from TableName that satisfy the WHERE condition. If WHERE clause
is ommitted, then all tuples are deleted and the relation schema for table name
is removed from the DB.
10. INPUT FileName1 [OUTPUT FileName2];
Reads and carries out the commands from FileName1. If OUTPUT FileName2 is
specified, the result is written to it. E.g. input a.dat; input a.dat output
b.dat;
11. EXIT
Terminates program execution. Note that the schemas and data should be saved
before exit and ready for use the next time the program is executed.
REQUIREMENTS:
Groups of one to four students are allowed. If students drop from a group, the
rest of the group is responsible for all work.
All searches based on primary key attribute must be done by first searching the
index, and if the key is matched, use the record pointer to access the
corresponding record from the file.
For tables with primary keys, when a search is on a non-key attribute, you must
perform an in-order traversal of the BST index and retrieve the table records
from the table in an order equivalent to the in-order traversal of the BST
index. This way, everyone will have the same answer for my test data, because
every student will have the same BST index but not necessarily the same order
for records in the table depending on how you implement deletions.
The tokens are separated by one or more white spaces (including tabs and CR),
plus statements may span multiple lines and only end with a semicolon. For
example,
USE
UIVERSITY
;
The grammar is not case sensitive, and comparisons in the DBMS is not case
sensitive either. Use and USE are the same. select … where X=”abc” should be
true if X is “Abc”. Also 1=1.0 should be true.
Make reasonable assumptions. Better yet, confirm with me if your assumption is
the right one.
Requirements (no exceptions) for implementation:
- Use recursive descent parsing for the grammar. You can look this up if you
don’t know what it is, or use whatever parsing technique you want.
- Each table has its own file. The heading of the file will contain the
description of the table e.g. # of attributes, Attribute names, types and
sizes. Following the header will be the tuples in the table.
- Use a separate file to save the BST index for a table.
- Your code must run in the COSC lab with only the software/libraries/add-ons
already installed.
I typically use the same the same input file/test process for all the
assignments. I don’t intend to adjust the test data for any of the submissions.
If you do not follow these requirements, you may end up with an overall score
of zero.
Rubric:
Numbers 1 to 10 are worth 10 points each. Each part must work completely for you to get
the 10 points. Partial credits may be granted depending on how close it is to the expected
outcome.