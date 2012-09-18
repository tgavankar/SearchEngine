import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Operator that performs the AND operation.
 */
public class AndOperator extends Operator {

	@Override
	public String getType() {
		return "AND";
	}

	/**
	 * Performs the set intersection operation (on Map Key) across all the input
	 * maps. Maintains the lowest seen value per key after intersection.
	 */
	@Override
	public Map<Integer, Integer> combine(List<Map<Integer, Integer>> l) {
		Map<Integer, Integer> out = new HashMap<Integer, Integer>();

		// Perform intersection
		if(l.size() > 0) {
			out = l.get(0);
			for(int i=1; i<l.size(); i++) {
				out.keySet().retainAll(l.get(i).keySet());
			}
		}
		
		// Maintain min value per key
		for(Entry<Integer, Integer> e : out.entrySet()) {
			int min = e.getValue();
			for(int i=0; i<l.size(); i++) {
				if(l.get(i).get(e.getKey()) < min) {
					min = l.get(i).get(e.getKey());
				}
			}
			out.put(e.getKey(), min);
		}

		return out;
	}

}
