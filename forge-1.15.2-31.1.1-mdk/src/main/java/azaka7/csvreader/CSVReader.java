package azaka7.csvreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class CSVReader {
	
	/*public static void main(String...strings) {
		File file = new File("test.csv");
		CSVFile csv = readFile(file);
		System.out.println("Reading "+file.getName());
		for(int i = 0; i < csv.rows(); i++) {
			for(String entry : csv.getRow(i)) {
				System.out.println(entry);
			}
		}
	}*/
	
	public static final CSVFile readFile(File file) {
		if(!file.exists()) {
			return null;
		}
		
		try {
			final ArrayList<String[]> lines = new ArrayList<String[]>();
			final BufferedReader reader = new BufferedReader(new FileReader(file));
			
			while(reader.ready()) {
				lines.add(parseLine(reader.readLine()));
				//lines.add(reader.readLine().split(","));
			}
			
			reader.close();
			return CSVFile.instantiate(file, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static final String[] parseLine(String raw) {
		final ArrayList<String> ret = new ArrayList<String>();
		
		final String[] input = raw.split(",");
		for(int i = 0; i < input.length; i++) {
			String line = input[i];
			while((new Character(line.charAt(line.length()-1))).equals('\\')) {
				line = line.substring(0, line.length()-1) + "," + (i+1 < input.length ? input[++i] : "");
			}
			if(line.contains(",")) {
				line = line.substring(1, line.length()-1);
			}
			ret.add(line);
			
		}
		
		return ret.toArray(new String[ret.size()]);
	}

}
