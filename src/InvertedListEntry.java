import java.util.List;
import java.util.Set;


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
	public List<Integer> getPositionSet() {
		return positionList;
	}
	public void setPositionList(List<Integer> positionList) {
		this.positionList = positionList;
	}
}
