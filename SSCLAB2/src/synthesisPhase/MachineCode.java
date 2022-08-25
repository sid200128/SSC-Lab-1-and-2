package synthesisPhase;

import java.util. * ;
import java.io.*;
//import java.nio.file.Files;
import java.util.Scanner;

public class MachineCode {
	static Hashtable<String, String> symbolTable = new Hashtable<>();
	public static void constructSymbolTable() {
		try {
			File symbolFile = new File("symbolTable.txt");
			Scanner sc = new Scanner(symbolFile);
			while (sc.hasNextLine()) {
				String eachLine = sc.nextLine();
				String tokens[] = eachLine.split(" ");
				symbolTable.put(tokens[0], tokens[2]);
			}
//			symbolTable.forEach((key, value) -> System.out.println(key + " "  + value));
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	public static void main(String[] args) {
		constructSymbolTable();
		try {
			File inputFile = new File("input.txt");
			Scanner sc = new Scanner(inputFile);
			File outputFile = new File("output.txt");
			FileWriter machineFile = new FileWriter(outputFile);
			
			while (sc.hasNextLine()) {
				String eachEntry = sc.nextLine();
				String rows[] = eachEntry.split("  ");
				boolean isAD = false;
				for (String row: rows) {
					if (row.indexOf("AD") != -1) {
						isAD = true;
						break;
					}
				}
				String outputEntry = "";
				if (isAD) {
//					System.out.println("AD or DL");
				} else {
					for (int i = 0; i<rows.length; i++) {
						if (i == 0 || rows[i].length() == 1) {
							outputEntry += rows[i] + " ";
						} else {
							String cmd[] = rows[i].split(", ");
							if (Objects.equals(cmd[0], "(S")) {
//								System.out.print(cmd[0] + "*\n");
								String number = cmd[1];
								int lenNum = number.length();
								String index = number.substring(0, lenNum-1);
								if (symbolTable.get(index) != null) {
									outputEntry += symbolTable.get(index) + " ";
								}
							} else {
								String number = cmd[1];
								int lenNum = number.length();
								String onlyNumber = number.substring(0, lenNum-1);
								outputEntry += onlyNumber + " ";
							}
						}
					}
				}
				outputEntry += "\n";
				System.out.print(outputEntry);
				machineFile.write(outputEntry);
			}
			machineFile.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}

}
