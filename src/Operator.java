import java.util.List;
import java.util.Map;


public abstract class Operator {
	public abstract String getType();
	
	
	public String toString() {
		return getType();
	}
	
	public abstract Map<Integer, Integer> combine(List<Map<Integer, Integer>> l);
}
