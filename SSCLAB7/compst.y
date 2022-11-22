%{
#include <stdio.h>
#include <stdlib.h>
extern int yylex();
extern int yyerror(char *str);
extern int yywrap();
extern int yyparse();
%}

%token WH IF DO FOR OP CP OCB CCB CMP SC ASG ID NUM COMMA OPR

%%
start: swh | mwh | dowh | for | mfor | if | mif
swh: WH OP cmpn CP stmt { printf("VALID SINGLE STATEMENT WHILE LOOP\n"); exit(0); };
mwh: WH OP cmpn CP OCB stmtlist CCB { printf("VALID MULTI STATEMENT WHILE LOOP\n"); exit(0); };
dowh: DO OCB stmtlist CCB WH OP cmpn CP SC { printf("VALID DO WHILE STATEMENT\n"); exit(0); };
for: FOR OP stmt cmpn SC ID ASG ID OPR NUM CP stmt { printf("VALID SINGLE STATEMENT FOR LOOP\n"); exit(0); };
mfor: FOR OP stmt cmpn SC ID ASG ID OPR NUM CP OCB stmtlist CCB { printf("VALID MULTI STATEMENT FOR LOOP\n"); exit(0); };
if: IF OP cmpn CP stmt{ printf("VALID SINGLE STATEMENT IF CONDITION\n"); exit(0); };
mif: IF OP cmpn CP OCB stmtlist CCB { printf("VALID MULTI STATEMENT IF CONDITION\n"); exit(0); };
cmpn: ID CMP ID | ID CMP NUM;
stmt: ID ASG ID OPR ID SC | ID ASG ID OPR NUM SC | ID ASG NUM OPR ID SC | ID ASG NUM OPR NUM SC | ID ASG ID SC | ID ASG NUM SC | start { printf("NESTED INSIDE A "); }
stmtlist: stmt | stmtlist stmt;
%%

int yyerror(char *str)
{
	printf("%s", str);
    return 1;
}

int main ()
{
    yyparse();
    return 0;
}
