import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


public class QueryRunner {	
	private boolean ranked;
	private static final InvertedListFactory factory = new InvertedListFactory(Config.invListDir); 
	
	public QueryRunner(boolean ranked) {
		this.ranked = ranked;
	}
	
	public QueryRunner() {
		this(false);
	}
	
	public List<Integer[]> run(Node root) {
		List<Map<Integer, Integer>> matches = new ArrayList<Map<Integer, Integer>>();
		List<Integer[]> out = new ArrayList<Integer[]>();
		
		Map<Integer, Integer> results = run(root, matches);
		
		for (Entry<Integer, Integer> entry  : entriesSortedByValues(results)) {
			Integer[] outa = new Integer[2];
			outa[0] = entry.getKey();
			outa[1] = entry.getValue();
		    out.add(outa);
		}
		
		return out;
	}
	
	private Map<Integer, Integer> run(Node root, List<Map<Integer, Integer>> matches) {
		if(root.getValue() != null) {
			InvertedList il = factory.getInvertedList(root.getValue(), root.getField());
			List<InvertedListEntry> ilel = il.getList();
			Map<Integer, Integer> invLMap = new HashMap<Integer, Integer>();
			for(InvertedListEntry ile : ilel) {
				invLMap.put(ile.getDocid(), ranked ? ile.getTotalFreq() : 1);	
			}
			matches.add(invLMap);
			return invLMap;
		}
		else {
			for(Node child : root.getChildren()) {
				if(root.getOperator().getType().equals("NEAR")) {
					runNear(root, matches);
				}
				else {
					run(child, matches);
				}
			}
		}
		return root.getOperator().combine(matches);
	}
	
	private Map<Integer, Integer> runNear(Node root, List<Map<Integer, Integer>> matches) {
		List<Map<Integer, InvertedListEntry>> allLists = new ArrayList<Map<Integer, InvertedListEntry>>();
		int maxDist = ((NearOperator) root.getOperator()).getDistance();
		
		for(Node child : root.getChildren()) {
			InvertedList il = factory.getInvertedList(child.getValue(), child.getField());
			List<InvertedListEntry> ilel = il.getList();
			Map<Integer, InvertedListEntry> docs = new HashMap<Integer, InvertedListEntry>();
			for(InvertedListEntry e : ilel) {
				docs.put(e.getDocid(), e);
			}
			allLists.add(docs);
		}
		
		//allLists = query words
		//allLists.get(i) = map of docIDs to InvertedListEntry for corresponding docID for ith query word
		//allLists.get(i).get(docid) = InvertedListEntry of docid of ith query word
		//allLists.get(i).keySet() = Set of all docIDs for ith query word

		// Get AND of all docids
		Set<Integer> docIds = new HashSet<Integer>();
		if(allLists.size() > 0) {
			docIds = allLists.get(0).keySet();
			for(int i=1; i<allLists.size(); i++) {
				docIds.retainAll(allLists.get(i).keySet());
			}
		}
		
		
		Map<Integer, Integer> nearMap = new HashMap<Integer, Integer>();
		for(int i=0; i<allLists.size()-1; i++) {
			Map<Integer, InvertedListEntry> curr = allLists.get(i);
			Map<Integer, InvertedListEntry> next = allLists.get(i+1);
			
			for(Integer doc : docIds) {
				for(Integer pos : curr.get(doc).getPositionSet()) {
					for(int k=1; k<=maxDist; k++) {
						if(next.get(doc).getPositionSet().contains(pos+k) && isNear(allLists, i+1, doc, pos+k, maxDist)) {
							// WE HAVE A MATCH FOR NEAR
							nearMap.put(doc, ranked ? (nearMap.containsKey(doc) ? nearMap.get(doc)+1 : 1) : 1);
							break;
						}
					}
				}
			}
		}
		matches.add(nearMap);
		return nearMap;
	}
	
	private boolean isNear(List<Map<Integer, InvertedListEntry>> allLists, int currI, int doc, int pos, int maxDist) {
		if(currI+2 > allLists.size()) {
			return true;
		}
		
		if(currI+2 == allLists.size()) {
			// base case, compare last 2 in list
			Map<Integer, InvertedListEntry> next = allLists.get(currI+1);
			
			for(int k=1; k<=maxDist; k++) {
				if(next.get(doc).getPositionSet().contains(pos+k)) {
					return true;
				}
			}
			return false;
		}
		else {
			Map<Integer, InvertedListEntry> next = allLists.get(currI+1);
			for(int k=1; k<=maxDist; k++) {
				if(next.get(doc).getPositionSet().contains(pos+k) && isNear(allLists, currI+1, doc, pos+k, maxDist)) {
					return true;
				}
			}
			return false;
		}
	}
	
	// Source: http://stackoverflow.com/questions/2864840/treemap-sort-by-value
	static <K extends Comparable<? super K>,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int res = e2.getValue().compareTo(e1.getValue());
                    int res2 = e1.getKey().compareTo(e2.getKey());
                    return res != 0 ? res : (res2 != 0 ? res2 : 1); // Special fix to preserve items with equal values
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
