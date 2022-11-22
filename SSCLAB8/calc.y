%{
	#include<stdio.h>
	#include <stdlib.h>
	#include <math.h>
%}

%union{
	char* fchar;
	double fval;
	int intval;
};

%token SIN
%token COS
%token TAN
%token <fchar> NAME
%token <intval> NUMBER
%type <fval> exp
%left '+''-'
%left '*''/'
%left '^'' '

%%
stmt: NAME'='exp {printf("=%f\t\n", $3);}
    | exp {printf("=%f\n", $1);}
    ;

exp: exp'+'exp {$$ = $1 + $3;}
   | exp'-'exp {$$ = $1 - $3;}
   | exp'*'exp {$$ = $1 * $3;}
   | SIN' 'exp {$$ = sin($3*3.14/180);}
   | COS' 'exp {$$ = cos($3*3.14/180);}
   | TAN' 'exp {$$ = tan($3*3.14/180);}
   | exp'/'exp {
	if ($3==0) {
		printf("\nCan't Divide by Zero\n");
		exit(1);
	} else {
		$$ = $1 / $3;
	}
   }
   | NUMBER {$$ = $1;}
   ;
%%

void yyerror (char *error) {
	printf("%s", error);
}

int main () {
	yyparse();
	return 0;
}
	
