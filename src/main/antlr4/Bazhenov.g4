grammar Bazhenov;

// Правила парсера
program: OPEN_CURLY (statement)* CLOSE_CURLY ;

statement: varDeclaration
         | assignment
         | ifStatement
         | forLoop
         | doWhileLoop
         | inputStatement
         | outputStatement ;

varDeclaration: DIM identifier (COMMA identifier)* type ;

assignment: LET? identifier EQUALS expression ;

ifStatement: IF expression THEN statement (ELSE statement)? END_ELSE ;

forLoop: FOR OPEN_PAREN assignment SEMICOLON expression SEMICOLON assignment CLOSE_PAREN statement ;

doWhileLoop: DO WHILE expression statement LOOP ;

inputStatement: INPUT OPEN_PAREN identifier (identifier)* CLOSE_PAREN ;

outputStatement: OUTPUT OPEN_PAREN expression (expression)* CLOSE_PAREN ;

expression: operand (relationOp operand)* ;
operand: addend (additionOp addend)* ;
addend: multiplier (multiplicationOp multiplier)* ;
multiplier: identifier | number | logicalConstant | unaryOp multiplier | OPEN_PAREN expression CLOSE_PAREN ;

// Операции
relationOp: NE | EQ | LT | LE | GT | GE ;
additionOp: PLUS | MIN | OR ;
multiplicationOp: MULT | DIV | AND ;
unaryOp: TILDE ;

// Идентификаторы и числа
identifier: IDENTIFIER ;
number: INTEGER | REAL ;

// Типы
type: WHOLE | VALID | LOGICAL ;

// Логические константы
logicalConstant: LOGICAL_CONST ;

logicalExpression: operand (logicalOp operand)* ;
logicalOp: AND | OR ;

//
// ЛЕКСЕМЫ ААААААААААААААААААААААААААААААААААААААА
//

// Операции
NE: 'NE' ;
EQ: 'EQ' ;
LT: 'LT' ;
LE: 'LE' ;
GT: 'GT' ;
GE: 'GE' ;
PLUS: 'plus' ;
MIN: 'min' ;
OR: 'or' ;
MULT: 'mult' ;
DIV: 'div' ;
AND: 'and' ;
TILDE: '~' ;

// Символы
OPEN_CURLY: '{' ;
CLOSE_CURLY: '}' ;
DIM: 'dim' ;
COMMA: ',' ;
LET: 'let' ;
EQUALS: '=' ;
IF: 'if' ;
THEN: 'then' ;
ELSE: 'else' ;
END_ELSE: 'end_else' ;
FOR: 'for' ;
OPEN_PAREN: '(' ;
CLOSE_PAREN: ')' ;
SEMICOLON: ';' ;
DO: 'do' ;
WHILE: 'while' ;
LOOP: 'loop' ;
INPUT: 'input' ;
OUTPUT: 'output' ;

// Типы
WHOLE: '%' ;
VALID: '!' ;
LOGICAL: '$' ;

// Идентификаторы и числа
IDENTIFIER: LETTER (LETTER | INTEGER)* ;
INTEGER: (BINARY+ ('B' | 'b') | OCTAL+ ('O' | 'o') | DIGIT+ ('D' | 'd')? | HEXDIGIT+ ('H' | 'h')) ;
REAL: (DIGIT+ '.' DIGIT* | '.' DIGIT+) (EXPONENT)? ;
LOGICAL_CONST: 'true' | 'false' ;

// Символы
LETTER: [a-zA-Z] ;
DIGIT: [0-9] ;
BINARY: '0' | '1';
OCTAL: [0-7] ;
HEXDIGIT: [0-9a-fA-F] ;
EXPONENT: ('E' | 'e') ('+' | '-')? DIGIT+ ;

// Пропускаемые символы
WS: [ \t\r\n]+ -> skip ;
COMMENT: '(*' .*? '*)' -> skip ;
