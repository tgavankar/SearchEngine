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
	public Map<Integer, Double> combine(List<Map<Integer, Double>> l) {
		Map<Integer, Double> out = new HashMap<Integer, Double>();
		
		for(Map<Integer, Double> m : l) {
			for(Entry<Integer, Double> e : m.entrySet()) {
				if(!out.containsKey(e.getKey())) {
					out.put(e.getKey(), (1 - e.getValue()));
				}
				else {
					out.put(e.getKey(), out.get(e.getKey()) * (1 - e.getValue()));
				}
			}
		}

		for(Entry<Integer, Double> e : out.entrySet()) {
			out.put(e.getKey(), (1 - e.getValue()));
		}
		
		return out;
	}
}
