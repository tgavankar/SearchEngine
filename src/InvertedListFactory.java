import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;


/**
 * Factory to parse the inverted list from disk into an InvertedList object.
 */
public class InvertedListFactory {
	private String invPath;
	
	/**
	 * Construct with path to directory of inverted list files.
	 */
	public InvertedListFactory(String p) {
		if(p.charAt(p.length()-1) != '/') {
			p = p + '/';
		}
		
		invPath = p;
	}
	
	/**
	 * Reads inverted list file from disk and parses it into InvertedList.
	 * 
	 * It reads from the FIELD titled subdirectory for appropriate field-matching.
	 */
	public InvertedList getInvertedList(String word, String field) {
		return parseFile(field + "/" + word);
	}
	
	/**
	 * Helper function that takes a canonical file name and outputs the
	 * inverted list. This method appends the file extension and directory path,
	 * so they must not be in the input.
	 */
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
					// First line of file, special parsing
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
			// Return the empty inverted list
			return invList;
		}
		return invList;
	}
}
