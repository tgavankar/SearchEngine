import java.util.List;
import java.util.Set;


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
