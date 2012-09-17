import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QueryRunner {	
	private boolean ranked;
	private static final InvertedListFactory factory = new InvertedListFactory("invlist"); 
	
	public QueryRunner(boolean ranked) {
		this.ranked = ranked;
	}
	
	public QueryRunner() {
		this(false);
	}
	
	public Map<Integer, Integer> run(Node root) {
		List<Map<Integer, Integer>> matches = new ArrayList<Map<Integer, Integer>>();
		return run(root, matches);
	}
	
	private Map<Integer, Integer> run(Node root, List<Map<Integer, Integer>> matches) {
		if(root.getValue() != null) {
			InvertedList il = factory.getInvertedList(root.getValue());
			List<InvertedListEntry> ilel = il.getList() != null ? il.getList() : new ArrayList<InvertedListEntry>();
			Map<Integer, Integer> invLMap = new HashMap<Integer, Integer>();
			for(InvertedListEntry ile : ilel) {
				invLMap.put(ile.getDocid(), ranked ? ile.getTotalFreq() : 1);	
			}
			matches.add(invLMap);
			return invLMap;
		}
		else {
			for(Node child : root.getChildren()) {
				run(child, matches);
			}
		}
		return root.getOperator().combine(matches);
	}
}
