import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Operator that performs the OR operation.
 */
public class OrOperator extends Operator {

	@Override
	public String getType() {
		return "OR";
	}

	/**
	 * Performs the set union operation across all the input maps and
	 * maintains the maximum value per key.
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
