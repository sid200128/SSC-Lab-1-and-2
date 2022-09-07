package macroPass1;

import java.util.*;
import java.io.*;


public class Pass1 {
	static Hashtable<Integer, MDTEntry> MDTtable = new Hashtable<Integer, MDTEntry>();
	static Hashtable<String, MNTEntry> MNTtable = new Hashtable<String, MNTEntry>();
	static Hashtable<String, ALAEntry> ALAtable = new Hashtable<String, ALAEntry>();
	
	public static void addMDT(Integer id, String definition) {
		MDTEntry entry = new MDTEntry();
		entry.id = id;
		entry.definition = definition;
		MDTtable.put(id, entry);
	}
	public static void addMNT(Integer id, String name, int mdtId) {
		MNTEntry entry = new MNTEntry();
		entry.id = id;
		entry.name = name;
		entry.mdtId = mdtId;
		MNTtable.put(name, entry);
	}
	public static void addALA(Integer id, String argument, String actual) {
		ALAEntry entry = new ALAEntry();
		entry.id = id;
		entry.argument = argument;
		entry.actual = actual;
		ALAtable.put(argument, entry);
	}
	public static void main(String[] args) {
		try {
			int mntId = 1;
			int mdtId = 1;
			int alaId = 1;
			
			File inputFile = new File("input.txt");
			FileWriter outputFile = new FileWriter("output.txt");
			Scanner sc = new Scanner(inputFile);
			
			while (sc.hasNextLine()) {
				
				String currLine = sc.nextLine();
				String tokens[] = currLine.split(" ");
			
				if (Objects.equals(tokens[0], "MACRO")) {
//					mntId = mdtId;
					while (!Objects.equals(tokens[0], "MEND")) {
						currLine = sc.nextLine();
						tokens = currLine.split(" ");
						
						if (Objects.equals(tokens[0], "MEND")) {
							addMDT(mdtId++, currLine);
							continue;
						}
						if (!Objects.equals(tokens[0], "")) {
							addMNT(mntId++, tokens[0], mdtId);
							addMDT(mdtId++, currLine);
							addALA(alaId++, tokens[1], "");
							addALA(alaId++, tokens[2], "");
						} else {
							String currNewLine = "";
							for (int i = 1; i<4; i++) {
								if (ALAtable.get(tokens[i]) != null) {
									currNewLine += "#" + String.valueOf(ALAtable.get(tokens[i]).id) + " ";
								} else {
									currNewLine += tokens[i] + " ";
								}
							}
							addMDT(mdtId++, currNewLine);
						}
					}
				} else {
					outputFile.write(currLine);
					outputFile.write("\n");
				}
			}
			outputFile.close();
			sc.close();
		} catch (Exception e) {
			
		}
		System.out.println("MDT TABLE");
		MDTtable.forEach((key, value) -> {
			System.out.println(value.id + " " + value.definition);
		});
		System.out.println("MNT TABLE");
		MNTtable.forEach((key, value) -> {
			System.out.println(value.id + " " + value.name + " " + value.mdtId);
		});
		System.out.println("ALA TABLE");
		ALAtable.forEach((key, value) -> {
			System.out.println(value.id + " " + value.argument + " " + value.actual);
		});
	}

}
