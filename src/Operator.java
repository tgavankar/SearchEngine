import java.util.List;
import java.util.Map;


/**
 * Abstract class for the Operators. All operators must extend this.
 */
public abstract class Operator {
	/**
	 * Returns the type of operator this is.
	 */
	public abstract String getType();
	
	/**
	 * String representation of operator.
	 */
	public String toString() {
		return getType();
	}
	
	/**
	 * Takes a list of map of docid to score, and combines them
	 * into a single map of docid to score. 
	 */
	public abstract Map<Integer, Double> combine(List<Map<Integer, Double>> matches);
}
