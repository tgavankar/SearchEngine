import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InvertedListFactory {
	private String invPath;
	public InvertedListFactory(String p) {
		if(p.charAt(p.length()-1) != '/') {
			p = p + '/';
		}
		invPath = p;
	}
	
	public InvertedList getInvertedList(String word, String field) {
		return parseFile(field + "/" + word);
	}
	
	private InvertedList parseFile(String name) {
		InvertedList invList = new InvertedList();
		try {
			FileInputStream fstream = new FileInputStream(invPath + name + ".inv");

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			String[] split;
			int lineNumber = 0;
			
			
			while ((strLine = br.readLine()) != null)   {
				split = strLine.split(" ");
				if(lineNumber == 0) {
					// First line of file
					invList.setTerm(split[0]);
					invList.setStemmedTerm(split[1]);
					invList.setCollectionTermFreq(Integer.parseInt(split[2]));
					invList.setTotalTermCount(Integer.parseInt(split[3]));
				}
				else {
					InvertedListEntry le = new InvertedListEntry();
					le.setDocid(Integer.parseInt(split[0]));
					le.setTotalFreq(Integer.parseInt(split[1]));
					le.setDocLength(Integer.parseInt(split[2]));
					Set<Integer> positions = new HashSet<Integer>();
					for(int i=3; i<split.length; i++) {
						positions.add(Integer.parseInt(split[i]));
					}
					le.setPositionSet(positions);
					invList.addEntry(le);
				}
				lineNumber++;
			}

			in.close();
		} catch (Exception e){
			//System.err.println("Error: " + e.getMessage());
			return invList;
		}
		return invList;
	}
}
