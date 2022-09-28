%{

%}


digit [0-9]
letter [a-zA-Z]
id ({letter}({letter}|{digit}|_)*)
space [' ']
package (import{space}java(\.{id})*(\.\*)?)
access_specifier (public|private|protected)
keywords (class|static|throws|try)
datatypes (void|int|char|float|boolean|String)
user_defined_classname (class{space}{id})
string_constant (\"w*\")
/*
predefined_classnames
*/
comma (,{space})
array_names ({id}\[\])
parameters ({datatypes}{space}({id}|{array_names}))
user_defined_functions ({datatypes}{space}{id}\(({parameters}(,{space})?)*\))
predefined_functions ({id}\(({id}(,{space})?)*\))
operators (\+|-|\*|\/|=|>|<|>=|<=|&|\||%|!|\^)
loops (for|while|do)
conditional (if|else|elseif)
/* {parameters} { printf("Parameters: %s\n", yytext); } */
/* line 39 */
%%

[\t ]+

{package} { printf("Package: %s\n", yytext); }
{user_defined_classname} { printf("User Defined Classname: %s\n", yytext); }
{user_defined_functions} { printf("User defined Function: %s\n", yytext); }
{predefined_functions} { printf("Predefined Function: %s\n", yytext); }
{array_names} { printf("Array Names: %s\n", yytext); }
{access_specifier} { printf("Access Specifier: %s\n", yytext); }
{operators} { printf("Operator: %s\n", yytext); }
{keywords} { printf("Keyword: %s\n", yytext); }
{datatypes} { printf("Datatype: %s\n", yytext); }
{id} { printf("Identifiers: %s\n", yytext); }
{string_constant} {printf("String Constant: %s\n", yytext); }
{comma} { printf("Comma: %s\n", yytext); }
.|\n
%%

int main() {
	yyin = fopen("input.txt", "r");
	yylex();
	yywrap();
	return 0;
}

int yywrap(void) {
	return 1;
}
