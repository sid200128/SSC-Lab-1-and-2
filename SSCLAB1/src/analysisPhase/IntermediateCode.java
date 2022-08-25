package analysisPhase;

import java.util. * ;
import java.io.*;
//import java.nio.file.Files;
import java.util.Scanner;

public class IntermediateCode {
	static int symbolID = 1;
	static Hashtable<String, Entry> SymbolTable = new Hashtable<>();
	public static int addSymbol(String name, int tokenno, int locationCounter) {
		// incase the symbol does not exist
		int id = -1;
		if (tokenno == 0) {
			// symbol is at label position
			if (SymbolTable.get(name) != null) {
				Entry row = SymbolTable.get(name);
				row.address = String.valueOf(locationCounter);
				id = row.id;
			} else {
				Entry row = new Entry();
				id = row.id = symbolID++;
				row.name = name;
				row.address = "-1";
				SymbolTable.put(name, row);
			}
		} else {
			// symbol is at operand position
			if (SymbolTable.get(name) == null) {
				Entry row = new Entry();
				id = row.id = symbolID++;
				row.name = name;
				row.address = "-1";
				SymbolTable.put(name, row);
			} else {
				Entry row = SymbolTable.get(name);
//				System.out.println(name);
				id = row.id;
			}
		}
		return id;
	}
	public static void main(String[] args) {
		Hashtable<String, String> IS = new Hashtable<>();
		Hashtable<String, String> DL = new Hashtable<>();
		Hashtable<String, String> AD = new Hashtable<>();
		Hashtable<String, String> REG = new Hashtable<>();
		Hashtable<String, String> CC = new Hashtable<>();
		IS.put("STOP", "(IS, 00)  0  0");
		IS.put("ADD", "(IS, 01)");
		IS.put("SUB", "(IS, 02)");
		IS.put("MULT", "(IS, 03)");
		IS.put("MOVER", "(IS, 04)");
		IS.put("MOVEM", "(IS, 05)");
		IS.put("COMP", "(IS, 06)");
		IS.put("BC", "(IS, 07)");
		IS.put("DIV", "(IS, 08)");
		IS.put("READ", "(IS, 09)  0");
		IS.put("PRINT", "(IS, 10)");
		DL.put("DC", "(DL, 01)  0 ");
		DL.put("DS", "(DL, 02)  0 ");
		AD.put("START", "---  (AD, 01)  0 ");
		AD.put("END", "---  (AD, 02)  0  0");
		AD.put("ORIGIN", "---  (AD, 03) ");
		AD.put("EQU", "---  (AD, 04)  0 ");
		AD.put("LTORG", "5");
		REG.put("AREG", " 1 ");
		REG.put("BREG", " 2 ");
		REG.put("CREG", " 3 ");
		REG.put("DREG", " 4 ");
		CC.put("LT", " 1 ");
		CC.put("LE", " 2 ");
		CC.put("EQ", " 3 ");
		CC.put("GT", " 4 ");
		CC.put("GE", " 5 ");
		CC.put("ANY", " 6 ");
		
		int locationCounter = 0;
		
		String outputEntry;
		String prevSymbol = "";
		try {
			File file = new File("input.txt");
			Scanner sc = new Scanner(file);
			File outputFile = new File("output.txt");
			FileWriter printFile = new FileWriter(outputFile);
			
			while (sc.hasNextLine()) {
				String row = sc.nextLine();
				
				outputEntry = "";
				
				StringTokenizer st = new StringTokenizer(row, " ");
				
				int numOfTokens = st.countTokens();
				
				for (int i = 0; i<numOfTokens; i++) {
					String token = st.nextToken();
//					System.out.println(token);
					if (IS.get(token) != null || DL.get(token) != null) {
						outputEntry = String.valueOf(locationCounter) + "  ";
					}
					if(IS.get(token) != null) {
//						if (i == 0) {
//							outputEntry = outputEntry + " -- ";
//						}
//						outputEntry = String.valueOf(locationCounter) + " ";
						if (Objects.equals(token, "STOP")) {
							outputEntry = outputEntry + IS.get(token);
							locationCounter++;
						} else if (Objects.equals(token, "BC")) {
							outputEntry = outputEntry + IS.get(token) + " ";
							token = st.nextToken();
							i++;
							outputEntry = outputEntry + CC.get(token);
							token = st.nextToken();
							i++;
							int id = addSymbol(token, i, locationCounter);
							outputEntry = outputEntry + " (S, " + String.valueOf(id) + ")";
//							System.out.println(outputEntry);
							locationCounter++;
						} else if (Objects.equals(token, "READ")) {
							outputEntry = outputEntry + IS.get(token) + " ";
							token = st.nextToken();
							i++;
							int id = addSymbol(token, i, locationCounter);
							outputEntry = outputEntry + " (S, " + String.valueOf(id) + ")";
							locationCounter++;
						} else if (Objects.equals(token, "PRINT")) {
							outputEntry = outputEntry + IS.get(token) + " ";
							token = st.nextToken();
							i++;
							int id = addSymbol(token, i, locationCounter);
							outputEntry = outputEntry + " (S, " + String.valueOf(id) + ")";
							locationCounter++;
						} else {
							outputEntry = outputEntry + IS.get(token) + " ";
							token = st.nextToken();
							i++;
							outputEntry = outputEntry + REG.get(token);
							token = st.nextToken();
							i++;
							int id = addSymbol(token, i, locationCounter);
							outputEntry = outputEntry + " (S, " + String.valueOf(id) + ")";
							locationCounter++;
						}
					} else if (AD.get(token) != null) {
						if (Objects.equals(token, "START")) {
							outputEntry = outputEntry + AD.get(token);
							token = st.nextToken();
							i++;
							locationCounter = Integer.parseInt(token);
							outputEntry = outputEntry + " (C, " + String.valueOf(locationCounter) + ")";
						} else if (Objects.equals(token, "END")) {
							outputEntry = outputEntry + AD.get(token);
						} else if (Objects.equals(token, "ORIGIN")) {
							outputEntry = outputEntry + AD.get(token);
							token = st.nextToken();
							i++;
							String inc = "";
							if (token.matches("[0-9]+")) {
								// only number
								locationCounter = Integer.parseInt(token);
								outputEntry = outputEntry + "(C, " + token + ")";
							} else if (token.matches("[a-zA-z]+")){
								// only symbol
								Entry oneEntry = SymbolTable.get(token);
								locationCounter = Integer.parseInt(oneEntry.address);
								outputEntry = outputEntry + " (S, " + String.valueOf(oneEntry.id) + ")";
							} else {
								// symbol+number
								StringTokenizer sp = new StringTokenizer(token, "+");
								String symbol = sp.nextToken();
								Entry oneEntry = SymbolTable.get(symbol);
								locationCounter = Integer.parseInt(oneEntry.address);
								inc = sp.nextToken();
								int num = Integer.parseInt(inc);
								locationCounter = locationCounter + num;
								outputEntry = outputEntry + " (S, " + String.valueOf(oneEntry.id) + ")+" + inc;
							}
							outputEntry = outputEntry + "  0";
						} else if (Objects.equals(token, "EQU")) {
							outputEntry = outputEntry + AD.get(token);
							token = st.nextToken();
							i++;
							Entry oneEntry = SymbolTable.get(token);
							addSymbol(prevSymbol, 0, Integer.parseInt(oneEntry.address));
							outputEntry = outputEntry + " (S, " + String.valueOf(oneEntry.id) + ")";
						}
					} else if (DL.get(token) != null) {
						if (Objects.equals(token, "DS")) {
							outputEntry = outputEntry + DL.get(token) + " (C, ";
							token = st.nextToken();
							i++;
							int num = Integer.parseInt(token);
							outputEntry = outputEntry + token + ")";
							locationCounter = locationCounter + num;
						} else if (Objects.equals(token, "DC")) {
							outputEntry = outputEntry + DL.get(token) + " (C, ";
							token = st.nextToken();
							i++;
							outputEntry = outputEntry + token + ")";
							locationCounter++;
						}
					} else {
						addSymbol(token, i, locationCounter);
						prevSymbol = token;
					}
				}
				outputEntry = outputEntry + "\n";
				System.out.print(outputEntry);
				printFile.write(outputEntry);
			}
			sc.close();
			printFile.close();
			
		} catch(Exception e) {
			System.out.println(e);
		}
//		System.out.println("\n****Symbol Table****");
		try {
			File symbolFile = new File("symbolTable.txt");
			FileWriter sym = new FileWriter(symbolFile);
			SymbolTable.forEach((key, value) -> {
					try {
						sym.write(value.id + " " + value.name + " " + value.address + "\n");
					} catch(Exception e) {
						System.out.println(e);
					}
				}
			);
			sym.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
