grammar Bazhenov;

// Основные структуры
program: '{' (declaration | statement)* '}' ;

declaration: 'dim' IDENTIFIER (',' IDENTIFIER)* type ;

type: TYPE_INTEGER | TYPE_REAL | TYPE_BOOLEAN ;

// Операторы
statement: compound
         | assignment
         | conditional
         | fixedLoop
         | conditionalLoop
         | input
         | output
         ;

compound: OPEN_CURLY statement (SEMICOLON statement)* CLOSE_CURLY ;
assignment: LET? IDENTIFIER EQUALS expression ;
conditional: IF expression THEN statement (ELSE statement)? END_ELSE ;
fixedLoop: FOR OPEN_PAREN assignment SEMICOLON expression SEMICOLON assignment CLOSE_PAREN statement ;
conditionalLoop: DO WHILE expression statement LOOP ;
input: INPUT OPEN_PAREN IDENTIFIER (IDENTIFIER)* CLOSE_PAREN ;
output: OUTPUT OPEN_PAREN expression (expression)* CLOSE_PAREN ;

// Выражения
expression: operand (relationOp operand)* ;
operand: summand (additionOp summand)* ;
summand: multiplier (multiplicationOp multiplier)* ;
multiplier: IDENTIFIER | INTEGER | REAL | logicalConstant | NOT multiplier ;

logicalConstant: TRUE | FALSE ;

// Операции
relationOp: NE | EQ | LT | LE | GT | GE ;
additionOp: PLUS | MIN | OR ;
multiplicationOp: MULT | DIV | AND ;

//
// ЛЕКСЕМЫ ААААААААААААААААААААААААААААААААААААААА
//

// Операции

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
TYPE_INTEGER: '%' ;
TYPE_REAL: '!' ;
TYPE_BOOLEAN: '$' ;

// Токены для группы операций отношения
NE: 'NE' ;
EQ: 'EQ' ;
LT: 'LT' ;
LE: 'LE' ;
GT: 'GT' ;
GE: 'GE' ;

// Токены для группы операций сложения
PLUS: 'plus' ;
MIN: 'min' ;
OR: 'or' ;

// Токены для группы операций умножения
MULT: 'mult' ;
DIV: 'div' ;
AND: 'and' ;

// Токен для унарной операции
NOT: '~' ;

// Токены для логических констант
TRUE: 'true' ;
FALSE: 'false' ;

// Токен для идентификаторов
IDENTIFIER: LETTER (LETTER | DIGIT)* ;
// Токены для чисел
INTEGER: BINARY | OCTAL | DECIMAL | HEX ;
REAL: DIGIT+ (E PLUS_MINUS? DIGIT+)? | (DIGIT+ '.') DIGIT+ (E PLUS_MINUS? DIGIT+)? ;

// Вспомогательные лексические правила
fragment LETTER: [a-zA-Z] ;
fragment DIGIT: [0-9] ;
fragment BINARY: BINARY_DIGIT+ ('B' | 'b') ;
fragment BINARY_DIGIT: '0' | '1' ;
fragment OCTAL: OCTAL_DIGIT+ ('O' | 'o') ;
fragment OCTAL_DIGIT: [0-7] ;
fragment DECIMAL: DIGIT+ ('D' | 'd')? ;
fragment HEX: HEX_DIGIT+ ('H' | 'h') ;
fragment HEX_DIGIT: DIGIT | [A-Fa-f] ;
fragment E: 'E' | 'e' ;
fragment PLUS_MINUS: '+' | '-' ;

// Пропускаемые символы
WS: [ \t\r\n]+ -> skip ;
COMMENT: '(*' .*? '*)' -> skip ;
