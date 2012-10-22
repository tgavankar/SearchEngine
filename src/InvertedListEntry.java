import java.util.List;
import java.util.Set;


/**
 * Class to represent a specific entry in an inverted list. Each entry contains 
 * information about which document the entry is for, as well as metadata such as
 * frequency, length, and a position list.
 */
public class InvertedListEntry {
	private int docid;
	private int totalFreq;
	private int docLength;
	private List<Integer> positionList;
	
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
	public List<Integer> getPositionList() {
		return positionList;
	}
	public void setPositionList(List<Integer> positionList) {
		this.positionList = positionList;
	}
}
