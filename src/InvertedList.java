import java.util.ArrayList;
import java.util.List;


public class InvertedList {
	private String term;
	private String stemmedTerm;
	private int collectionTermFreq;
	private int totalTermCount;
	
	private List<InvertedListEntry> list;
	
	public InvertedList() {
		list = new ArrayList<InvertedListEntry>();
	}
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getStemmedTerm() {
		return stemmedTerm;
	}

	public void setStemmedTerm(String stemmedTerm) {
		this.stemmedTerm = stemmedTerm;
	}

	public int getCollectionTermFreq() {
		return collectionTermFreq;
	}

	public void setCollectionTermFreq(int collectionTermFreq) {
		this.collectionTermFreq = collectionTermFreq;
	}

	public int getTotalTermCount() {
		return totalTermCount;
	}

	public void setTotalTermCount(int totalTermCount) {
		this.totalTermCount = totalTermCount;
	}

	public List<InvertedListEntry> getList() {
		return list;
	}
	
	public void addEntry(InvertedListEntry e) {
		this.list.add(e);
	}
}
