import java.util.ArrayList;
import java.util.List;


/**
 * Class to represent the inverted list for a term.
 */
public class InvertedList {
	private String term;
	private String stemmedTerm;
	private int collectionTermFreq;
	private int totalTermCount;
	private int docFreq;
	
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

	public void setDocFreq(int docFreq) {
		this.docFreq = docFreq;
	}
	
	public int getDocFreq() {
		return this.docFreq;
	}
	
	public InvertedListEntry getDocIdEntry(int id) {
        int lo = 0;
        int hi = list.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if(id < list.get(mid).getDocid()) 
            	hi = mid - 1;
            else if (id > list.get(mid).getDocid()) 
            	lo = mid + 1;
            else 
            	return list.get(mid);
        }
        return new InvertedListEntry(); // not found
	}
}
