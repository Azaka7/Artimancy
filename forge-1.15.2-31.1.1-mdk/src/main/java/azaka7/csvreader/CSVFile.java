package azaka7.csvreader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CSVFile {
	
	private final File fileLocation;
	private final List<String[]> contents;
	
	private CSVFile(File location, List<String[]> data) {
		fileLocation = location;
		contents = data;
	}
	
	public static CSVFile instantiate(File loc, List<String[]> dat) {
		if(!loc.exists() || !loc.canRead()) { return null; }
		return new CSVFile(loc,dat);
	}
	
	public File getFile() {
		return fileLocation;
	}
	
	public int rows() {
		return contents.size();
	}
	
	/**Does not return a reliable value. Only the first row is measured.*/
	public int columns() {
		return contents.get(0).length;
	}
	
	public String get(int row, int column) {
		String[] rowAt = getRow(row);
		if(rowAt == null || rowAt.length >= column) {return "";}
		else {
			return rowAt[column];
		}
	}
	
	public Iterator<String[]> getRowIterator(){
		return contents.iterator();
	}
	
	public String[] getRow(int index) {
		return contents.get(index).clone();
	}
	
	public String[] getColumn(int index) {
		final List<String> ret = new ArrayList<String>(contents.size());
		getRowIterator().forEachRemaining(line -> {
			if(line.length > index) {
				ret.add(line[index]);
			} else {
				ret.add("");
			}
		});
		return ret.toArray(new String[ret.size()]);
	}
	
}
