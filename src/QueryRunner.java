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


/**
 * Class that takes a query tree's root Node and executes
 * the represented query.
 * 
 * A single instance of QueryRunner can run multiple
 * queries, provided Config does not need to change.
 */
public class QueryRunner {	
	private boolean ranked;
	private static final InvertedListFactory factory = new InvertedListFactory(Config.invListDir); 
	
	public QueryRunner(boolean ranked) {
		this.ranked = ranked;
	}
	
	/**
	 * The default constructor for this class runs
	 * unranked matching.
	 */
	public QueryRunner() {
		this(false);
	}
	
	/**
	 * Runs the given query and returns the list of ranked docIDs with their score.
	 */
	public List<Integer[]> run(Node root) {
		List<Map<Integer, Integer>> matches = new ArrayList<Map<Integer, Integer>>();
		List<Integer[]> out = new ArrayList<Integer[]>();
		
		Map<Integer, Integer> results = run(root, matches);
		
		// Get elements sorted by score and then docID
		for (Entry<Integer, Integer> entry  : entriesSortedByValues(results)) {
			Integer[] outa = new Integer[2];
			outa[0] = entry.getKey();
			outa[1] = entry.getValue();
		    out.add(outa);
		}
		
		return out;
	}
	
	/**
	 * Recursive helper function that takes a Node and runs its query, appending
	 * the resulting Map (of docID to score) to the matches List. The final return value
	 * is the combined score map of DocID to score, combined with the appropriate
	 * score combination method. 
	 */
	private Map<Integer, Integer> run(Node root, List<Map<Integer, Integer>> matches) {
		if(root.getValue() != null) {
			// Node is a value Node, so just get the inverted list.
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
			// NOde is an operator Node, so get all the children and apply operator.
			for(Node child : root.getChildren()) {
				// Special case for handling NEAR operator
				if(root.getOperator().getType().equals("NEAR")) {
					runNear(root, matches);
				}
				// Recurse on other operators
				else {
					run(child, matches);
				}
			}
		}
		
		// Combine the results based on the parent's score combination method
		return root.getOperator().combine(matches);
	}
	
	/**
	 * Special case helper for handling the NEAR operator to get the
	 * combined inverted list for documents that match.
	 */
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
				// Do intersection of all docs in all query words so that we are only
				// left with the docIDs that have all the terms. Discarding all docIDs
				// where all terms do not exist is good because NEAR can never match them.
				docIds.retainAll(allLists.get(i).keySet());
			}
		}
		
		
		Map<Integer, Integer> nearMap = new HashMap<Integer, Integer>();
		for(int i=0; i<allLists.size()-1; i++) {
			Map<Integer, InvertedListEntry> curr = allLists.get(i);
			Map<Integer, InvertedListEntry> next = allLists.get(i+1);
			
			// Loop through all the docIDs
			for(Integer doc : docIds) {
				// Loop through all the positions for the first word in NEAR
				for(Integer pos : curr.get(doc).getPositionSet()) {
					// Loop through all possible positions for the succeeding word
					for(int k=1; k<=maxDist; k++) {
						// Check that the next word is within maxDist of prev AS WELL as 
						// recursing on the word after next being within maxDist of next,
						// etc.
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
	
	/**
	 * Helper function that recursively returns if words are successive in a doc (within maxDist).
	 */
	private boolean isNear(List<Map<Integer, InvertedListEntry>> allLists, int currI, int doc, int pos, int maxDist) {
		if(currI+2 > allLists.size()) {
			// We're at the end of word list, so no more left to compare
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
			// Recursive step, check current and next being within maxDist and recurse on next pair.
			Map<Integer, InvertedListEntry> next = allLists.get(currI+1);
			for(int k=1; k<=maxDist; k++) {
				if(next.get(doc).getPositionSet().contains(pos+k) && isNear(allLists, currI+1, doc, pos+k, maxDist)) {
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * Helper function that returns the SortedSet of a Map where it is
	 * first sorted on decreasing values of the map, and then sorted on
	 * increasing keys of the map. 
	 * 
	 * Based on: http://stackoverflow.com/questions/2864840/treemap-sort-by-value
	 */
	private static <K extends Comparable<? super K>,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int res = e2.getValue().compareTo(e1.getValue());
                    int res2 = e1.getKey().compareTo(e2.getKey());
                    return res != 0 ? res : (res2 != 0 ? res2 : 1); // Sort by key for equal values
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
