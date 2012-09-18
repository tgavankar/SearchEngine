import java.util.Set;


/**
 * Class to represent a specific entry in an inverted list. Each entry contains 
 * information about which document the entry is for, as well as metadata such as
 * frequency, length, and a position set (for quick contains() calls).
 */
public class InvertedListEntry {
	private int docid;
	private int totalFreq;
	private int docLength;
	private Set<Integer> positionSet;
	
	public int getDocid() {
		return docid;
	}
	public void setDocid(int docid) {
		this.docid = docid;
	}
	public int getTotalFreq() {
		return totalFreq;
	}
	public void setTotalFreq(int totalFreq) {
		this.totalFreq = totalFreq;
	}
	public int getDocLength() {
		return docLength;
	}
	public void setDocLength(int docLength) {
		this.docLength = docLength;
	}
	public Set<Integer> getPositionSet() {
		return positionSet;
	}
	public void setPositionSet(Set<Integer> positionSet) {
		this.positionSet = positionSet;
	}
}
