#include <bits/stdc++.h>

using namespace std;

int i = 0;
string user_string;

void E();
void EP();
void T();
void TP();
void F();

void E() {
	T();
	EP();
}

void EP() {
	if (i<user_string.size() && user_string[i] == '+') {
		i++;
		T();
		EP();
	}
}

void F() {
	if (i < user_string.size() && user_string[i] == '(') {
		i++;
		E();
		if (i < user_string.size() && user_string[i] == ')') {
		  i++;
		} else {
			cout << "String Not Accepted" << endl;
			exit(1);
		}
	} else if (i < user_string.size() && user_string[i] == 'i'
    && i+1 < user_string.size() && user_string[i+1] == 'd') i += 2; else {
		cout  << "String Not Accepted" << endl;
		exit(1);
	}
}

void T() {
	F();
	TP();
}

void TP() {
	if (i < user_string.size() && user_string[i] == '*') {
		i++;
		F();
		TP();
	}
}



int main() {
	while (1) {
		cout << "Enter the string: ";
		cin >> user_string;
		i = 0;
		E();
		if (i == user_string.size()) cout << "String Accepted" << endl;
		else cout << "String Not Accepted" << endl;
		cout << "Do you want to continue[Y/N]?" << endl;
		char option;
		cin >> option;
		if (option == 'N' || option == 'n') exit(0);
	}
	return 0;
}
