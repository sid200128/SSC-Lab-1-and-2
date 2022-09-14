package macroPass2;

import java.util.*;
import java.io.*;

public class Pass2 {
	static Hashtable<Integer, MDTEntry> MDTtable = new Hashtable<Integer, MDTEntry>();
	static Hashtable<String, MNTEntry> MNTtable = new Hashtable<String, MNTEntry>();
	static Hashtable<String, ALAEntry> ALAtable = new Hashtable<String, ALAEntry>();
	public static void init_mdt() {
		try {
			File mdtFile = new File("mdt.txt");
			Scanner sc = new Scanner(mdtFile);
			while (sc.hasNextLine()) {
				String tokens[] = sc.nextLine().split(" ");
				int id = Integer.valueOf(tokens[0]);
				String def = "";
				for (int i = 1; i<tokens.length; i++) {
					def += tokens[i] + " ";
				}
				MDTEntry entry = new MDTEntry();
				entry.id = id;
				entry.definition = def;
				MDTtable.put(id, entry);
			}
			sc.close();
		} catch(Exception E) {
			System.out.println(E);
		}
	}
	public static void init_mnt() {
		try {
			File mntFile = new File("mnt.txt");
			Scanner sc = new Scanner(mntFile);
			while (sc.hasNextLine()) {
				String tokens[] = sc.nextLine().split(" ");
				int id = Integer.valueOf(tokens[0]);
				String name = tokens[1];
				int mdtId = Integer.valueOf(tokens[2]);
				MNTEntry entry = new MNTEntry();
				entry.id = id;
				entry.name = name;
				entry.mdtId = mdtId;
				MNTtable.put(name, entry);
			}
			sc.close();
		} catch(Exception E) {
			System.out.println(E);
		}
	}
	public static void init_ala() {
		try {
			File alaFile = new File("ala.txt");
			Scanner sc = new Scanner(alaFile);
			while (sc.hasNextLine()) {
				String tokens[] = sc.nextLine().split(" ");
				int id = Integer.valueOf(tokens[0]);
				String argument = tokens[1];
				ALAEntry entry = new ALAEntry();
				entry.id = id;
				entry.argument = argument;
				String id_entry = "#" + tokens[0];
				ALAtable.put(id_entry, entry);
			}
			sc.close();
		} catch(Exception E) {
			System.out.println(E);
		}
	}
	public static void main(String[] args) {
		init_mdt();
		init_mnt();
		init_ala();
		try {
			File inputFile = new File("input.txt");
			FileWriter outputFile = new FileWriter("output.txt");
			Scanner sc = new Scanner(inputFile);
			
			while (sc.hasNextLine()) {
				String currLine = sc.nextLine();
				String tokens[] = currLine.split(" ");
				if (MNTtable.get(tokens[0]) != null) {
					MNTEntry entry = MNTtable.get(tokens[0]);
					int mdtId = entry.mdtId;
					MDTEntry currEntry = MDTtable.get(mdtId);
					mdtId++;
					String def = currEntry.definition;
					String arguments[] = def.split(" ");
					
					for (int i = 1; i<arguments.length; i++) {
						String id = "-1";
						ALAEntry ala_entry = new ALAEntry();
						Set<String> setOfKeys = ALAtable.keySet();
						for (String key: setOfKeys) {
							ala_entry = ALAtable.get(key);
							id = key;
							if (Objects.equals(ala_entry.argument, arguments[i])) break;
						}
						ala_entry.actual = tokens[i];
						ALAtable.replace(id, ala_entry);
					}
					while (!Objects.equals(def.split(" ")[0], "MEND")) {
						String outputLine = "";
						currEntry = MDTtable.get(mdtId);
						System.out.println(mdtId);
						def = currEntry.definition;
						mdtId++;
						if (Objects.equals(def.split(" ")[0], "MEND")) continue;
						
						String lineTokens[] = def.split(" ");
						
						for (int i = 0; i<lineTokens.length; i++) {
							if (ALAtable.get(lineTokens[i]) != null) {
								ALAEntry alaentry = ALAtable.get(lineTokens[i]);
								outputLine += alaentry.actual + " ";
							} else {
								outputLine += lineTokens[i] + " ";
							}
						}
						outputLine += "\n";
						outputFile.write(outputLine);
					}
				} else {
					currLine += "\n";
					outputFile.write(currLine);
				}
			}
			outputFile.close();
			sc.close();
			ALAtable.forEach((key, value) -> {
				String entry = value.id + " " + value.argument + " " + value.actual;
				System.out.println(entry);
			});
		} catch (Exception E) {
			System.out.println(E);
		}
		
	}

}
