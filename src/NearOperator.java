import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Operator that performs the NEAR operation.
 */
public class NearOperator extends Operator {
	private int distance;
	
	public NearOperator(int dist) {
		distance = dist;
	}
	
	@Override
	public String getType() {
		return "NEAR";
	}

	public int getDistance() {
		return distance;
	}
	
	public String toString() {
		return getType() + ":" + distance;
	}

	/**
	 * Performs a set union, keeping the max score (similar to OR).
	 */
	@Override
	public Map<Integer, Integer> combine(List<Map<Integer, Integer>> l) {
		Map<Integer, Integer> out = new HashMap<Integer, Integer>();
		for(Map<Integer, Integer> m : l) {
			for(Entry<Integer, Integer> e : m.entrySet()) {
				if(!out.containsKey(e.getKey())) {
					out.put(e.getKey(), e.getValue());
				}
				else {
					out.put(e.getKey(), Math.max(out.get(e.getKey()), e.getValue()));
				}
			}
		}

		return out;
	}
}
